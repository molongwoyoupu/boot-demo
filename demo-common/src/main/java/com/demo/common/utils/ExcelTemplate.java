package com.demo.common.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.demo.common.annotation.ExcelTitle;
import com.demo.common.exception.Asserts;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

/**
 * excel导入导出工具类
 *
 * @author molong
 * @date 2021/9/6
 */
public class ExcelTemplate {

    private static final Logger logger = LoggerFactory.getLogger(ExcelTemplate.class);

    /**
     * 导出数据
     * 注意 : 1)泛型T上面要加上ExcelTitle注解,可以参见 EmployeePageVO ,导出只会寻找加了注解的列,,不加注解默认全部导出
     *       2)若为枚举类型请加上枚举相关的参数 参见 EmployeePageVO :: status 字段
     * @param response  响应
     * @param objects  导出数据
     * @param titleName  标题
     */
    public static <T> void export(HttpServletResponse response, List<T> objects, String titleName) {
        export(response,objects,titleName,null);
    }

    /**
     * 导出数据
     * 注意 : 1)泛型T上面要加上ExcelTitle注解,导出只会寻找加了注解的列,,不加注解默认全部导出
     *       2)若为枚举类型请加上枚举相关的参数
     * @param response 响应
     * @param objects  导出数据
     * @param titleName 标题
     * @param fileName  导出文件名
     */
    public static <T> void export(HttpServletResponse response, List<T> objects,String titleName,String fileName) {
        List listMap = new ArrayList<>(objects.size());
        // 通过工具类创建writer，默认创建xls格式
        int merge = 0;
        ExcelWriter writer = ExcelUtil.getWriter();
        if (CollectionUtil.isNotEmpty(objects)) {
            T obj = objects.get(0);
            List<String> filedNames = createHeader(obj,writer);
            if(CollectionUtil.isEmpty(filedNames)){
                listMap.addAll(objects);
            }else{
                merge = filedNames.size()-1;
                for (T p:objects) {
                    Class<?> objClass = p.getClass();
                    Field[] objClassDeclaredFields = objClass.getDeclaredFields();
                    Map<String,Object> bean = new HashMap<>(objClassDeclaredFields.length);
                    Stream.of(objClassDeclaredFields).forEach(f -> {
                        String filedName = f.getName();
                        if(filedNames.contains(filedName)){
                            ExcelTitle title = f.getAnnotation(ExcelTitle.class);
                            try {
                                f.setAccessible(true);
                                Method omethod = objClass.getMethod("get" + filedName.substring(0,1).toUpperCase() + filedName.substring(1));
                                Object value = omethod.invoke(p);
                                if(title.hasEnum()){
                                    Class<?> cl = title.enumClass();
                                    String methodName = title.enumMethod();
                                    Class<?> argClass = title.enumMethodArgClass();
                                    Method method = cl.getMethod(methodName,argClass);
                                    value = method.invoke(null,value);
                                }
                                bean.put(filedName,value);
                            } catch (Exception e) {
                                logger.error("导出数据异常;{}{}",titleName,e);
                            }

                        }
                    });
                    listMap.add(bean);
                }
            }

        }
        if (titleName != null && merge != 0) {
            // 这里做一个校验,在没有标题的时候,可作为模板使用
            writer.merge(merge,titleName);
        }
        writer.write(listMap, true);
        //response为HttpServletResponse对象
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        //弹出下载对话框的文件名
        if(StringUtils.isEmpty(fileName)){
            response.setHeader("Content-Disposition", "attachment;filename="+ UUID.randomUUID() +".xls");
        }else{
            String name = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            response.setHeader("Content-Disposition", "attachment;filename=" + name +".xls");
        }
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            logger.error("导出数据,流关闭异常;",e);
        }

        // 一次性写出内容，使用默认样式，强制输出标题
        writer.flush(out, true);
        writer.autoSizeColumnAll();
        // 关闭writer，释放内存
        writer.close();
        //此处记得关闭输出Servlet流
        IoUtil.close(out);
    }

    /**
     * 创建表头
     * @param obj       数据实体
     * @param writer    写入器
     * @return  字段名集合
     */
    private static List<String> createHeader(Object obj,ExcelWriter writer){
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        List<String> filedNames = new ArrayList<>();
        Stream.of(fields).forEach(f -> {
            ExcelTitle title = f.getAnnotation(ExcelTitle.class);
            if(title!=null){
                filedNames.add(f.getName());
                writer.addHeaderAlias(f.getName(), title.value());
            }
        });
        return filedNames;
    }

    /**
     * 将excel中的内容转化为预定义的bean
     * @param file 文件流
     * @param cls bean class
     * @param <V> bean 类型
     * @return 产生的bean
     * @throws IOException 抛出该错误自行接受处理
     */
    public static <V> List<V> importBeans(MultipartFile file, Class<V> cls) throws IOException {
        ExcelReader reader =  new ExcelReader(file.getInputStream(),0);
        //表头校验,看导入的是不是模板
        List<Map<String,Object>> firstRow = reader.read(0,0,1);
        String excelTitleStr = null;
        if (CollectionUtil.isNotEmpty(firstRow)) {
            excelTitleStr = String.join("", CollectionUtil.sort(firstRow.get(0).keySet(),
                    String::compareTo));
        }
        if (excelTitleStr == null) {
            Asserts.fail("请使用模板进行导入");
        }
        Map<String,String> alias = getHeaderAlias(cls);
        String aliaStr = String.join("", CollectionUtil.sort(alias.keySet(), String::compareTo));
        if(!aliaStr.equals(excelTitleStr)) {
            Asserts.fail("请使用模板进行导入");
        }
        //设置alias
        reader.setHeaderAlias(getHeaderAlias(cls));
        //不能忽略空行,不然行数计算不准确
        reader.setIgnoreEmptyRow(false);
        return reader.readAll(cls);
    }

    public static Map<String,String> getHeaderAlias(Class<?> cls) {
        Map<String,String> alis = new LinkedHashMap<>(16);
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            ExcelTitle title = field.getAnnotation(ExcelTitle.class);
            if (title != null) {
                alis.put(title.value(),name);
            }
        }
        return alis;
    }

    /**
     * 导入文件
     * @param file      文件
     * @param clazz     类型
     * @param heardRow  头行数
     * @param startRow  开始行数
     * @param endRow    结束行数
     * @param <T>   泛型
     * @return 数据集合
     */
    public static <T> List<T> importFile(MultipartFile file, Class<T> clazz, Integer heardRow,Integer startRow,Integer endRow){
        List<T> list = new ArrayList<>();
        ExcelReader reader = null;
        try {
            reader = ExcelUtil.getReader(file.getInputStream());
            list = reader.read(heardRow,startRow,endRow,clazz);
        } catch (Exception e) {
            logger.error("批量读取数据失败",e);
        }finally {
            if(!Objects.isNull(reader)){
                reader.close();
            }
        }
        return list;
    }


    /**
     * 导出excel表格
     *
     * @param fileName    文件名称
     * @param workbook   表格组装实体
     * @param response    响应对象
     */
    public static void downExcelByWorkbook(String fileName, Workbook workbook,
                                           HttpServletResponse response){
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(
                    (fileName + ".xls").getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            ServletOutputStream out = response.getOutputStream();
            try (BufferedInputStream bis = new BufferedInputStream(is); BufferedOutputStream bos = new BufferedOutputStream(out)) {
                byte[] buff = new byte[2048];
                int bytesRead;
                // Simple read/write loop.
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
            }
        }catch (IOException e){
            logger.error("excel导出异常",e);
        }
    }
}

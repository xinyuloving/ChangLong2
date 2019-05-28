package com.lkkdesign.changlong.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.poi.CustomXWPFDocument;
import com.lkkdesign.changlong.utils.DateUtil;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PoiActivity extends AppCompatActivity {
    //创建生成的文件地址
//    private static final String newPath = "/storage/emulated/0/test.docx";
    EditText output;
    private SXSSFWorkbook wb = null;//大数据导出格式

    private XSSFSheet sheet;

    private String sheetName;

    private String[] headers;//表头字段

    private Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }

        output = (EditText) findViewById(R.id.textOut);

        Button go = (Button) findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    public void onWriteClick(View view) {
        //printlnToUser("writing xlsx file");
        XSSFWorkbook workbook = new XSSFWorkbook();
        sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName("mysheet"));
        String[] title = {"订单", "店名", "电话", "地址"};//列名

        setHeadStyle(title);
        //getHeadStyle();

        for (int i = 0; i < 10; i++) {
            Row row = sheet.createRow(i);
            for (int j = 0; j < 10; j++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(i);
            }
        }
        String outFileName = "filetoshare.xlsx";
        try {
            printlnToUser("writing file " + outFileName);
            File cacheDir = getCacheDir();
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ChangLongData");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File outFile = new File(dir, outFileName);
            OutputStream outputStream = new FileOutputStream(outFile.getAbsolutePath());
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            printlnToUser("xlsx文件已经生成");
            //share(outFileName, getApplicationContext());
        } catch (Exception e) {
            /* proper exception handling to be here */
            printlnToUser(e.toString());
        }
    }

    //似乎没用到
    public Cell setHeadStyle(String[] titles) {
        Cell cell = null;

//        CellStyle headStyle = getHeadStyle();

        Row headRow = sheet.createRow(0);
        // 构建表头
        for (int i = 0; i < titles.length; i++) {
            cell = headRow.createCell(i);
//            cell.setCellStyle(headStyle);
            cell.setCellValue(titles[i]);
        }
        return cell;
    }

    /**
     * print line to the output TextView
     *
     * @param str
     */
    private void printlnToUser(String str) {
        final String string = str;
        if (output.length() > 8000) {
            CharSequence fullOutput = output.getText();
            fullOutput = fullOutput.subSequence(5000, fullOutput.length());
            output.setText(fullOutput);
            output.setSelection(fullOutput.length());
        }
        output.append(string + "\n");
    }

    public void share(String fileName, Context context) {
        Uri fileUri = Uri.parse("content://" + getPackageName() + "/" + fileName);
        printlnToUser("sending " + fileUri.toString() + " ...");
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        shareIntent.setType("application/octet-stream");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
    }

    private void initData() {
        Map<String, String> map = new HashMap<String, String>();
        Map<String, String> map2 = new HashMap<String, String>();
        map.put("$TITLE$", "长隆水质监测报告");//$TITLE$只是个标识符，你也可以用${}，[]等等
        map.put("$P1$", "打一个段落");
        map.put("$P2$", "第二个段落");
        map2.put("$T1$", "表格第一行第一列");
        map2.put("$C1$", "表格第一行第二列");
        map2.put("$T2$", "吸光度");
        map2.put("$C2$", "12.00");
        map.put("$P3$", "第三个段落");
        map.put("$C3$", "页脚中的内容");
        map.put("$T4$", "页脚表格第一行");
        map.put("$C4$", "页脚第一行内容");
        map.put("$T5$", "页脚表格第二行");
        map.put("$C5$", "页脚第二行内容");
        try {
            //读取示例文书
            InputStream is = getAssets().open("template.docx");
            //自定义的XWPFDocument，解决官方版图片不显示问题
            CustomXWPFDocument document = new CustomXWPFDocument(is);
            //读取段落（一般段落，页眉页脚没办法读取）
            List<XWPFParagraph> listParagraphs = document.getParagraphs();
            processParagraphs(listParagraphs, map);

            //读取页脚
            List<XWPFFooter> footerList = document.getFooterList();
            processParagraph(footerList, map);

            //处理表格
            Iterator<XWPFTable> it = document.getTablesIterator();
            while (it.hasNext()) {//循环操作表格
                XWPFTable table = it.next();
                List<XWPFTableRow> rows = table.getRows();
                for (XWPFTableRow row : rows) {//取得表格的行
                    List<XWPFTableCell> cells = row.getTableCells();
                    for (XWPFTableCell cell : cells) {//取得单元格
                        if ("$IMG$".equals(cell.getText())) {
                            //直接插入图片会在文档的最底端，所以要插在固定位置，要把图片放在表格里
                            //所以使用判断单元格，并清除单元格放置图片的方式来实现图片定位
                            cell.removeParagraph(0);
                            XWPFParagraph pargraph = cell.addParagraph();
                            document.addPictureData(getAssets().open("1.png"), XWPFDocument.PICTURE_TYPE_PNG);
                            document.createPicture(document.getAllPictures().size() - 1, 120, 120, pargraph);
                        }
                        List<XWPFParagraph> paragraphListTable = cell.getParagraphs();
                        processParagraphs(paragraphListTable, map2);
                    }
                }
            }
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ChangLongData");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String outDocxFileName = DateUtil.getNowDateTime2() + "simpleWriteDocx.docx";
            File outFile = new File(dir, outDocxFileName);
            OutputStream os = new FileOutputStream(outFile.getAbsolutePath());
            FileOutputStream fopts = new FileOutputStream(outFile.getAbsolutePath());
//            FileOutputStream fopts = new FileOutputStream(newPath);
            document.write(fopts);
            if (fopts != null) {
                fopts.close();
            }
            Toast.makeText(PoiActivity.this, "文档已生成", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //处理页脚中的段落，其实就是用方法读取了下页脚中的内容，然后也会当做一般段落处理
    private void processParagraph(List<XWPFFooter> footerList, Map<String, String> map) {
        if (footerList != null && footerList.size() > 0) {
            for (XWPFFooter footer : footerList) {
                //读取一般段落
                List<XWPFParagraph> paragraphs = footer.getParagraphs();
                processParagraphs(paragraphs, map);
                //处理表格
                List<XWPFTable> tables = footer.getTables();
                for (int i = 0; i < tables.size(); i++) {
                    XWPFTable xwpfTable = tables.get(i);
                    List<XWPFTableRow> rows = xwpfTable.getRows();
                    for (XWPFTableRow row : rows) {//取得表格的行
                        List<XWPFTableCell> cells = row.getTableCells();
                        for (XWPFTableCell cell : cells) {//取得单元格
                            List<XWPFParagraph> paragraphListTable = cell.getParagraphs();
                            processParagraphs(paragraphListTable, map);
                        }
                    }
                }
            }
        }
    }

    //处理段落
    public static void processParagraphs(List<XWPFParagraph> paragraphList, Map<String, String> param) {
        if (paragraphList != null && paragraphList.size() > 0) {
            for (XWPFParagraph paragraph : paragraphList) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    String text = run.getText(0);
                    if (text != null) {
                        boolean isSetText = false;
                        for (Map.Entry<String, String> entry : param.entrySet()) {
                            String key = entry.getKey();
                            if (text.indexOf(key) != -1) {
                                isSetText = true;
                                Object value = entry.getValue();
                                if (value instanceof String) {//文本替换
                                    text = text.replace(key, value.toString());
                                }
                            }
                        }
                        if (isSetText) {
                            run.setText(text, 0);
                        }
                    }
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        intent.setClass(this, ListEmReportActivity.class);
        startActivity(intent);
        this.finish();
    }

    // 停止自动翻页
    @Override
    protected void onStop() {
        super.onStop();
        PoiActivity.this.finish();
    }

    // 停止自动翻页
    @Override
    protected void onDestroy() {
        super.onDestroy();
        PoiActivity.this.finish();
    }

}

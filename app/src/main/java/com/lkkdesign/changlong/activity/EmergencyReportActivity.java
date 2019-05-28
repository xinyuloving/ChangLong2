package com.lkkdesign.changlong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.poi.CustomXWPFDocument;
import com.lkkdesign.changlong.utils.CustomToast;
import com.lkkdesign.changlong.utils.DateUtil;
import com.lkkdesign.changlong.widgets.MyProcessDialog;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class EmergencyReportActivity extends AppCompatActivity {

    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int MP = ViewGroup.LayoutParams.MATCH_PARENT;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_p1)
    EditText etP1;
    @BindView(R.id.textView10)
    TextView textView10;
    @BindView(R.id.et_p2)
    EditText etP2;
    @BindView(R.id.et_p3)
    EditText etP3;
    @BindView(R.id.et_p4)
    EditText etP4;
    @BindView(R.id.textView11)
    TextView textView11;
    @BindView(R.id.textView12)
    TextView textView12;
    @BindView(R.id.et_p5)
    EditText etP5;
    @BindView(R.id.et_p6)
    EditText etP6;
    @BindView(R.id.et_p7)
    EditText etP7;
    @BindView(R.id.et_org)
    EditText etOrg;
    @BindView(R.id.et_date)
    EditText etDate;
    @BindView(R.id.btn_calculate)
    Button btnCalculate;

    private TableLayout tableLayout;
    private Intent intent =  new Intent();
    private final String TAG = "EmergencyReportActivity";
    private MyProcessDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_report);
        ButterKnife.bind(this);

        dialog = new MyProcessDialog(EmergencyReportActivity.this,"处理中……");

        etDate.setText(DateUtil.getDate());
        initTableData(5, 7);

    }


    private void initTableData(int intRow, int int_Col) {
        //获取控件tableLayout
        tableLayout = (TableLayout) findViewById(R.id.table);
        //清除表格所有行
        tableLayout.removeAllViews();
        //全部列自动填充空白处
        tableLayout.setStretchAllColumns(true);

        TableRow tableRow_h = new TableRow(EmergencyReportActivity.this);
        //tv用于显示
        TextView tv_h1 = new TextView(EmergencyReportActivity.this);
        //Button bt=new Button(MainActivity.this);
        tv_h1.setBackgroundResource(R.drawable.rectangle);
        tv_h1.setText("序号");
        tv_h1.setTextColor(getResources().getColor(R.color.black));
        tableRow_h.addView(tv_h1);
        //tableLayout.addView(tableRow_h, new TableLayout.LayoutParams(MP, WC, 1));

        TextView tv_h2 = new TextView(EmergencyReportActivity.this);
        //Button bt=new Button(MainActivity.this);
        tv_h2.setBackgroundResource(R.drawable.rectangle);
        tv_h2.setText("监测点位");
        tv_h2.setTextColor(getResources().getColor(R.color.black));
        tableRow_h.addView(tv_h2);

        TextView tv_h3 = new TextView(EmergencyReportActivity.this);
        //Button bt=new Button(MainActivity.this);
        tv_h3.setBackgroundResource(R.drawable.rectangle);
        tv_h3.setText("苯");
        tv_h3.setTextColor(getResources().getColor(R.color.black));
        tableRow_h.addView(tv_h3);

        TextView tv_h4 = new TextView(EmergencyReportActivity.this);
        //Button bt=new Button(MainActivity.this);
        tv_h4.setBackgroundResource(R.drawable.rectangle);
        tv_h4.setText("甲苯");
        tv_h4.setTextColor(getResources().getColor(R.color.black));
        tableRow_h.addView(tv_h4);

        TextView tv_h5 = new TextView(EmergencyReportActivity.this);
        //Button bt=new Button(MainActivity.this);
        tv_h5.setBackgroundResource(R.drawable.rectangle);
        tv_h5.setText("二甲苯");
        tv_h5.setTextColor(getResources().getColor(R.color.black));
        tableRow_h.addView(tv_h5);

        TextView tv_h6 = new TextView(EmergencyReportActivity.this);
        //Button bt=new Button(MainActivity.this);
        tv_h6.setBackgroundResource(R.drawable.rectangle);
        tv_h6.setText("乙苯");
        tv_h6.setTextColor(getResources().getColor(R.color.black));
        tableRow_h.addView(tv_h6);

        TextView tv_h7 = new TextView(EmergencyReportActivity.this);
        //Button bt=new Button(MainActivity.this);
        tv_h7.setBackgroundResource(R.drawable.rectangle);
        tv_h7.setText("萘");
        tv_h7.setTextColor(getResources().getColor(R.color.black));
        tableRow_h.addView(tv_h7);

        tableLayout.addView(tableRow_h, new TableLayout.LayoutParams(MP, WC, 1));

        //生成X行，Y列的表格
        for (int i = 1; i <= intRow; i++) {
            TableRow tableRow = new TableRow(EmergencyReportActivity.this);
            for (int j = 1; j <= int_Col; j++) {
                //tv用于显示
                TextView tv = new TextView(EmergencyReportActivity.this);
                //Button bt=new Button(MainActivity.this);
                tv.setBackgroundResource(R.drawable.rectangle);
                tv.setText("(" + i + " , " + j + ")");
                tv.setTextColor(getResources().getColor(R.color.red));

                tableRow.addView(tv);
            }
            //新建的TableRow添加到TableLayout

            tableLayout.addView(tableRow, new TableLayout.LayoutParams(MP, WC, 1));

        }
    }

    @OnClick(R.id.btn_calculate)
    public void onViewClicked() {
        dialog.show();
        initData();
    }

    private void initData() {
//        dialog.show();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("$title$", etTitle.getText().toString());//$TITLE$只是个标识符，你也可以用${}，[]等等
        map.put("$paragraph1$", etP1.getText().toString());
        map.put("$paragraph2$", etP2.getText().toString());
        map.put("$paragraph3$", etP3.getText().toString());
        map.put("$paragraph4$", etP4.getText().toString());
        map.put("$paragraph5$", etP5.getText().toString());
        map.put("$paragraph6$", etP6.getText().toString());
        map.put("$paragraph7$", etP7.getText().toString());
        map.put("$organization$", etOrg.getText().toString());
        map.put("$date$", etDate.getText().toString());
        Log.i(TAG,"etTitle="+etTitle.getText());
        Log.i(TAG,"etP1="+etP1.getText());
        Log.i(TAG,"etP2="+etP2.getText());

        try {
            //读取示例文书
            InputStream is = getAssets().open("EmergencyReportTemplate.docx");
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
                        processParagraphs(paragraphListTable, map);
                    }
                }
            }

            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ChangLongData");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String outDocxFileName = DateUtil.getNowDateTime2()+ "应急报告.docx";
            File outFile = new File(dir, outDocxFileName);
            OutputStream os = new FileOutputStream(outFile.getAbsolutePath());
            FileOutputStream fopts = new FileOutputStream(outFile.getAbsolutePath());
//            FileOutputStream fopts = new FileOutputStream(newPath);
            document.write(fopts);
            if (fopts != null) {
                fopts.close();
            }
            Toast.makeText(EmergencyReportActivity.this,"文档已生成",Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(EmergencyReportActivity.this,"文档出现错误",Toast.LENGTH_LONG).show();
        }
        dialog.dismiss();
    }

    //处理页脚中的段落，其实就是用方法读取了下页脚中的内容，然后也会当做一般段落处理
    private void processParagraph(List<XWPFFooter> footerList, Map<String, Object> map) {
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
    public static void processParagraphs(List<XWPFParagraph> paragraphList, Map<String, Object> param) {
        if (paragraphList != null && paragraphList.size() > 0) {
            for (XWPFParagraph paragraph : paragraphList) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    String text = run.getText(0);
                    if (text != null) {
                        boolean isSetText = false;
                        for (Map.Entry<String, Object> entry : param.entrySet()) {
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
        EmergencyReportActivity.this.finish();
    }

    // 停止自动翻页
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EmergencyReportActivity.this.finish();
    }

}

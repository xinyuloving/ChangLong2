package com.lkkdesign.changlong.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.adapter.ImageAdapter;
import com.lkkdesign.changlong.config.Constants;
import com.lkkdesign.changlong.holder.LocalImageHolderView;
import com.lkkdesign.changlong.utils.CustomToast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

/**
* @author huangyaoyu
* @date 2019/5/9 9:08
* @desc
*/
public class Main2Activity extends AppCompatActivity implements OnItemClickListener {

    private Intent intent = new Intent();
    private Toolbar mNormalToolbar;
    private TextView tvUser;

    private ConvenientBanner convenientBanner;//顶部广告栏控件
    private ArrayList<Integer> localImages = new ArrayList<Integer>();
    private List<String> networkImages;
    private String[] images = {
            "http://img2.3lian.com/2014/f2/37/d/40.jpg",
            "http://d.3987.com/sqmy_131219/001.jpg",
            "http://img2.3lian.com/2014/f2/37/d/39.jpg",
            "http://www.8kmm.com/UploadFiles/2012/8/201208140920132659.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg"
    };

    private ListView listView;

    private GridView gview;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    private String strUserName = "";
    // 图片封装为一个数组
    private int[] icon = {
            R.drawable.icon_main_zd,
            R.drawable.icon_main_sd,
            R.drawable.icon_main_time,
            R.drawable.icon_main_gdj,
            R.drawable.icon_main_jz,
            R.drawable.icon_main_find,
            R.drawable.icon_main_yjbg,
            R.drawable.icon_main_set,
            R.drawable.icon_main_dy
    };
    private String[] iconName = {"自动测量", "手动测量", "定时测量", "光度计", "曲线校准", "历史数据", "应急报告",
            "设置", "蓝牙打印"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
//        Intent getIntent = getIntent();
//        strUserName = getIntent.getStringExtra("userName");

        initViews();
        init();

    }

    private void initViews() {

        mNormalToolbar = (Toolbar) findViewById(R.id.toolbar_normal);
        tvUser = (TextView) findViewById(R.id.tv_user);
        tvUser.setText(Constants.strLoginName);
        tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(Main2Activity.this, UserInfoActivity.class);
                startActivity(intent);
                Main2Activity.this.finish();
            }
        });
//        mNormalToolbar.setTitle(strUserName);
        initToolbar();

        convenientBanner = (ConvenientBanner) findViewById(R.id.convenientBanner);
        gview = (GridView) findViewById(R.id.gridview);
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        getData();
        //新建适配器
        String[] from = {"image", "text"};
        int[] to = {R.id.image, R.id.text};
        sim_adapter = new SimpleAdapter(this, data_list, R.layout.gridview_item, from, to);
        //配置适配器
        gview.setAdapter(sim_adapter);

//        //取得GridView对象
//        GridView gridview = (GridView) findViewById(R.id.gridview);
//        //添加元素给gridview
//        gridview.setAdapter(new ImageAdapter(this));
//
//        // 设置Gallery的背景
////        gridview.setBackgroundResource(R.drawable.bg0);
//
        //事件监听
        gview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Toast.makeText(Main2Activity.this, "你选择了" + (position + 1) + " 号图片", Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0://自动测量
                        intent.setClass(Main2Activity.this, AutoMeasureActivity.class);
                        intent.putExtra("from", "Auto");
                        startActivity(intent);
                        break;
                    case 1://手动测量
                        intent.setClass(Main2Activity.this, ManualMeasureFristActivity.class);
                        intent.putExtra("from", "manual");
                        startActivity(intent);
                        break;
                    case 2://定时测量
//                        intent.setClass(Main2Activity.this, TimingSetupActivity.class);
//                        startActivity(intent);
                        jumpToActivity(TimingSetupActivity.class);
                        break;
                    case 3://光度计
//                        intent.setClass(Main2Activity.this, PhotometerFristActivity.class);
//                        startActivity(intent);
                        jumpToActivity(PhotometerFristActivity.class);
                        break;
                    case 4: //曲线校准
                        intent.setClass(Main2Activity.this, CurveSelectActivity.class);
                        intent.putExtra("from", "xiaozhun");
                        startActivity(intent);
                        break;
                    case 5: //历史数据
//                        intent.setClass(Main2Activity.this, BaseSMRecycleViewActivity.class);
//                        startActivity(intent);
                        jumpToActivity(BaseSMRecycleViewActivity.class);
                        break;
                    case 6: //应急报告
//                        intent.setClass(Main2Activity.this, TBSActivity.class);
//                        intent.setClass(Main2Activity.this, ListEmReportActivity.class);
//                        startActivity(intent);
                        jumpToActivity(ListEmReportActivity.class);
                        break;
                    case 7: //设置
//                        intent.setClass(Main2Activity.this, SettingsActivity.class);
//                        startActivity(intent);
                        jumpToActivity(SettingsActivity.class);
                        break;
                    case 8: //帮助说明
                        intent.setClass(Main2Activity.this, com.lkkdesign.changlong.printer.AppStart.class);
                        intent.putExtra("type","main");
                        startActivity(intent);
//                        CustomToast.showToast(Main2Activity.this,"功能正在完善中，敬请期待……");
                        break;
                    default:
                        break;
                }
            }
        });

    }

    private void initToolbar() {
        //设置menu
        mNormalToolbar.inflateMenu(R.menu.main);
        //设置menu的点击事件
        mNormalToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int menuItemId = item.getItemId();
                if (menuItemId == R.id.action_about) {
//                    Toast.makeText(Main2Activity.this, "action_about", Toast.LENGTH_SHORT).show();
                    intent.setClass(Main2Activity.this,AboutActivity.class);
                    startActivity(intent);

                } else if (menuItemId == R.id.action_exit) {
//                    Toast.makeText(Main2Activity.this, "action_exit", Toast.LENGTH_SHORT).show();
                    final AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(Main2Activity.this);
                    normalDialog.setIcon(R.mipmap.icon_app);
                    normalDialog.setTitle(R.string.dialog_title);
                    normalDialog.setMessage(R.string.dialog_content_exit);
                    normalDialog.setPositiveButton(R.string.confirm,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    System.exit(0);
                                }
                            });
                    normalDialog.setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //...To-do
                                }
                            });
                    // 显示
                    normalDialog.show();
                }else{
                    CustomToast.showToast(Main2Activity.this,"功能正在完善中，敬请期待……");
                }
                return true;
            }
        });
        //设置左侧NavigationIcon点击事件
        mNormalToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Main2Activity.this, "点击了左侧按钮", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public List<Map<String, Object>> getData() {
        //cion和iconName的长度是相同的，这里任选其一都可以
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }

        return data_list;
    }


    private void init() {
//        initImageLoader();
        loadTestDatas();
        //本地图片例子
        convenientBanner.setPages(
                new CBViewHolderCreator() {
                    @Override
                    public LocalImageHolderView createHolder(View itemView) {
                        return new LocalImageHolderView(itemView);
                    }

                    @Override
                    public int getLayoutId() {
                        return R.layout.item_localimage;
                    }
                }, localImages)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                .setOnItemClickListener(this);
        //设置指示器的方向
//                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
//                .setOnPageChangeListener(this)//监听翻页事件
        ;

//        convenientBanner.setManualPageable(false);//设置不能手动影响

        //网络加载例子
//        networkImages=Arrays.asList(images);
//        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
//            @Override
//            public NetworkImageHolderView createHolder() {
//                return new NetworkImageHolderView();
//            }
//        },networkImages);


//手动New并且添加到ListView Header的例子
//        ConvenientBanner mConvenientBanner = new ConvenientBanner(this,false);
//        mConvenientBanner.setMinimumHeight(500);
//        mConvenientBanner.setPages(
//                new CBViewHolderCreator<LocalImageHolderView>() {
//                    @Override
//                    public LocalImageHolderView createHolder() {
//                        return new LocalImageHolderView();
//                    }
//                }, localImages)
//                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
//                        //设置指示器的方向
//                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
//                .setOnItemClickListener(this);
//        listView.addHeaderView(mConvenientBanner);
    }

    //    //初始化网络图片缓存库
//    private void initImageLoader(){
//        //网络图片例子,结合常用的图片缓存库UIL,你可以根据自己需求自己换其他网络图片库
//        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
//                showImageForEmptyUri(R.drawable.ic_default_adimage)
//                .cacheInMemory(true).cacheOnDisk(true).build();
//
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//                getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
//                .threadPriority(Thread.NORM_PRIORITY - 2)
//                .denyCacheImageMultipleSizesInMemory()
//                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
//                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
//        ImageLoader.getInstance().init(config);
//    }
    /*
    加入测试Views
    * */
    private void loadTestDatas() {
        //本地图片集合
        for (int position = 0; position < 4; position++)
            localImages.add(getResId("ic_test_" + position, R.mipmap.class));


////        //各种翻页效果
//        transformerList.add(DefaultTransformer.class.getSimpleName());
//        transformerList.add(AccordionTransformer.class.getSimpleName());
//        transformerList.add(BackgroundToForegroundTransformer.class.getSimpleName());
//        transformerList.add(CubeInTransformer.class.getSimpleName());
//        transformerList.add(CubeOutTransformer.class.getSimpleName());
//        transformerList.add(DepthPageTransformer.class.getSimpleName());
//        transformerList.add(FlipHorizontalTransformer.class.getSimpleName());
//        transformerList.add(FlipVerticalTransformer.class.getSimpleName());
//        transformerList.add(ForegroundToBackgroundTransformer.class.getSimpleName());
//        transformerList.add(RotateDownTransformer.class.getSimpleName());
//        transformerList.add(RotateUpTransformer.class.getSimpleName());
//        transformerList.add(StackTransformer.class.getSimpleName());
//        transformerList.add(ZoomInTransformer.class.getSimpleName());
//        transformerList.add(ZoomOutTranformer.class.getSimpleName());

//        transformerArrayAdapter.notifyDataSetChanged();
    }

    private void jumpToActivity(Class activityClass) {
        startActivity(new Intent(this, activityClass));
        this.finish();
    }

    /**
     * 通过文件名获取资源id 例子：getResId("icon", R.drawable.class);
     *
     * @param variableName
     * @param c
     * @return
     */
    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // 开始自动翻页
    @Override
    protected void onResume() {
        super.onResume();
//        //开始自动翻页
        convenientBanner.startTurning();
    }

    // 停止自动翻页
    @Override
    protected void onPause() {
        super.onPause();
//        //停止翻页
        convenientBanner.stopTurning();
    }

    // 停止自动翻页
    @Override
    protected void onStop() {
        super.onStop();

        Main2Activity.this.finish();
    }


    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "点击了第" + position + "个", Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(this, HeaderActivity.class));
//        convenientBanner.setCanLoop(!convenientBanner.isCanLoop());

    }
}
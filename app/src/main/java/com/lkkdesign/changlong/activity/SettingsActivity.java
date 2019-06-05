package com.lkkdesign.changlong.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.config.Constants;
import com.lkkdesign.changlong.utils.AppSharePreferenceMgr;
import com.lkkdesign.changlong.utils.CustomToast;
import com.lkkdesign.changlong.utils.DateUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    private static final int REQUEST_CODE_PERMISSION_SD = 101;
    private static final int REQUEST_CODE_SETTING = 300;
    Context mContext = null;
    private ListPreference preference_countdown;
    private Intent intent = new Intent();


    //此处在其他手机上运行出现Bug，需要调试。
//    SharedPreferences settings = getSharedPreferences("setTime", 0);
//    SharedPreferences.Editor editor = settings.edit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 从资源文件中添Preferences ，选择的值将会自动保存到SharePreferences
        addPreferencesFromResource(R.xml.preferences);
        mContext = getApplicationContext();

        init();
        String str_list_CountDown = (String) AppSharePreferenceMgr.get(this, "list_CountDown", "120");
//        System.out.println("保存的数据CountDown：" + str_list_CountDown);
        Constants.intCountDwonTime = Integer.parseInt(str_list_CountDown) * 1000;


    }


    private void init() {

        final EditTextPreference serverip = (EditTextPreference) findPreference("machineId");
        final EditTextPreference epCT = (EditTextPreference) findPreference("ep_CT");
        final EditTextPreference epCF = (EditTextPreference) findPreference("ep_CF");
        final EditTextPreference accountTime=(EditTextPreference)findPreference("accountTime");
        SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(this);

        serverip.setSummary(shp.getString("machineId", ""));
        SharedPreferences settings = getSharedPreferences("setTime", 0);
        epCT.setSummary(settings.getString("epCT", ""));
        epCF.setSummary(settings.getString("epCF", ""));
        SwitchPreference spPlayAuto = (SwitchPreference) findPreference("sp_playAuto");

        serverip.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                serverip.setSummary(newValue.toString());
                serverip.setDefaultValue(newValue);
                AppSharePreferenceMgr.put(getApplication(), "machineId", "" + newValue);
                return true;
            }
        });
        accountTime.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                accountTime.setSummary(newValue.toString());
                accountTime.setDefaultValue("120");
                AppSharePreferenceMgr.put(getApplication(),"accountTime",""+newValue);
                DateUtil.intCountDwonTime=Long.valueOf(String.valueOf(newValue))*1000;
                return true;
            }
        });

        epCT.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                epCT.setSummary(newValue.toString());
                epCT.setDefaultValue(newValue);
                Log.i("SettingActivity", "222=" + newValue.toString());
                Log.i("SettingActivity", "222=" + newValue);
                //AppSharePreferenceMgr.put(getApplication(), "epCT", "" + newValue);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("epCT", "" + newValue);
                editor.commit();
                return true;
            }
        });

        epCF.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                epCF.setSummary(newValue.toString());
                epCF.setDefaultValue(newValue);
                Log.i("SettingActivity", "333=" + newValue.toString());
                Log.i("SettingActivity", "333=" + newValue);
                //AppSharePreferenceMgr.put(getApplication(), "epCF", "" + newValue);
//                SharedPreferences settings = getSharedPreferences("setTime", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("epCF", "" + newValue);
                editor.commit();
                return true;
            }
        });

        preference_countdown = (ListPreference) findPreference("list_CountDown");
        preference_countdown.setOnPreferenceChangeListener(this);
        // 设置summary为所选中的值列表值
        if (preference_countdown.getEntry() != null) {
            preference_countdown.setSummary(preference_countdown.getEntry());//初始化时设置summary
            Log.d("Count1", "onPreferenceChange runpreference_countdown.getEntry() " + preference_countdown.getEntry());
            Constants.intCountDwonTime = Integer.parseInt(preference_countdown.getEntry().toString());
        }

        CheckBoxPreference mCheckboxBoot = (CheckBoxPreference) findPreference("checkbox_boot");
        mCheckboxBoot.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //这里可以监听到这个CheckBox 的点击事件
                return true;
            }
        });

        mCheckboxBoot.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference arg0, Object newValue) {
                //这里可以监听到checkBox中值是否改变了
                //并且可以拿到新改变的值
                CustomToast.showToast(mContext, "开机启动APP：" + (Boolean) newValue);
                return true;
            }
        });

        Preference preCheckUpdate = (Preference) findPreference("checkupdate");
        Preference preCopyright = (Preference) findPreference("copyright");
        Preference preExitapp = (Preference) findPreference("exitapp");
        Preference preexhibitgoods = (Preference) findPreference("exhibitgoods");
        Preference prebqdyj = (Preference) findPreference("bqdyj");
        Preference preBackAccount = findPreference("backaccount");

//        Preference preopenDoor = (Preference) findPreference("openDoor");
//        Preference precloseDoor = (Preference) findPreference("closeDoor");
//        //设置Preference的点击事件监听
//        preopenDoor.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                //当接收到Click事件之后触发
//                //Application.sendPortData_Hex(ComDoor, Constants.strOpenAllDooor);//发送打开所有柜门扫码指令
//                return true;
//            }
//        });
//        //设置Preference的点击事件监听
//        precloseDoor.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                //当接收到Click事件之后触发
//                //Application.sendPortData_Hex(ComDoor, Constants.strCloseAllDooor);//发送关闭所有柜门扫码指令
//                return true;
//            }
//        });
/**
 * 注销账号
 */
        preBackAccount.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                intent.setClass(SettingsActivity.this,AccountActivity.class);

//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setClass(SettingsActivity.this, LoginActivity.class);
//                SharedPreferences settings = getSharedPreferences("UserInfo", 0);
//                SharedPreferences.Editor editor = settings.edit();
//                editor.putString("Username", "");
//                editor.putString("Password", "");
//                editor.commit();
                startActivity(intent);
                return true;
            }
        });
        spPlayAuto.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                CustomToast.showToast(mContext, newValue.toString());
                return true;
            }
        });

        prebqdyj.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                intent.setClass(SettingsActivity.this, com.lkkdesign.changlong.printer.AppStart.class);
                startActivity(intent);
                return true;
            }
        });
        preCheckUpdate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //当接收到Click事件之后触发
                //CustomToast.showToast(mContext, "Preference preCheckUpdate Clicked");
                //getVersion(vision);
//                dialogUpdate();
//                booGetRequest_stop = false;
//                booUpdateThread_stop = false;
                return true;
            }
        });
        preCopyright.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //当接收到Click事件之后触发
                CustomToast.showToast(mContext, "暂无版权信息");
                return true;
            }
        });
        preExitapp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //当接收到Click事件之后触发
                new MaterialDialog.Builder(SettingsActivity.this)// 初始化建造者
//                        .icon(R.mipmap.icon_exit)
                        .title("温馨提示")// 标题
                        .content("确定要返回主页面？")// 内容
                        .positiveText(R.string.confirm)
                        .negativeText(R.string.cancel)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

//                                CustomToast.showToast(mContext, "退出程序");
//                                System.exit(0);
                                intent.setClass(SettingsActivity.this, Main2Activity.class);
                                startActivity(intent);
                                SettingsActivity.this.finish();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        })
                        .show();// 显示对话框

                return true;
            }
        });
        preexhibitgoods.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //当接收到Click事件之后触发
                CustomToast.showLongToast(mContext, "测量设置已还原到最初状态！");
//                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
//                startActivity(intent);
//                SettingsActivity.this.finish();
                return true;
            }
        });

//        MyDialogPreference myDialogPreference = (MyDialogPreference) findPreference("myDialogPreference");
//        myDialogPreference.setDialogTitle("提示");//设置title：
//        myDialogPreference.setDialogMessage("是否退出当前程序");//设置message
//        myDialogPreference.setPositiveButtonText("确定");//设置positivebutton，不过这里不能设置监听
//        myDialogPreference.setNegativeButtonText("取消");//同上
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        /**
         * newValue 返回的值是getEntries的值
         */
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;//把preference这个Preference强制转化为ListPreference类型
            CharSequence[] entries = listPreference.getEntries();//获取ListPreference中的实体内容
            int index = listPreference.findIndexOfValue((String) newValue);//获取ListPreference中的实体内容的下标值
            listPreference.setSummary(entries[index]);//listPreference中的sumamry显示为当前ListPreference的实体内容中选择的
            if ("list_CountDown".equals(listPreference.getKey())) {
                System.out.println("list_CountDown\n");
                AppSharePreferenceMgr.put(this, "list_CountDown", entries[index].toString());
            } else {
                System.out.println("list_AD\n");
                AppSharePreferenceMgr.put(this, "list_AD", entries[index].toString());
            }
            Log.d("TAG2", "onPreferenceChange run " + newValue);
            Log.i("TAG0", "listPreference " + listPreference.getKey());
            Toast.makeText(mContext, entries[index].toString(), Toast.LENGTH_LONG).show();
        }
//        if (preference instanceof EditTextPreference) {
//            serverip.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//
//                @Override
//                public boolean onPreferenceChange(Preference preference, Object newValue) {
//                    // TODO Auto-generated method stub
//                    serverip.setSummary(newValue.toString());
//                    serverip.setDefaultValue(newValue);
//                    Log.i("SettingActivity", "111=" + newValue.toString());
//                    Log.i("SettingActivity", "111=" + newValue);
////                                                       Constants.machineId = Integer.parseInt(newValue.toString());
//                    AppSharePreferenceMgr.put(getApplication(), "machineId", newValue.toString());
//                    return true;
//                }
//            });
//        }
        //AppSharePreferenceMgr.put(getApplication(), "machineId", Integer.parseInt(newValue.toString()));
        return true;
    }


    /**
     * 监听返回键
     */
    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
        intent.setClass(SettingsActivity.this, Main2Activity.class);
        startActivity(intent);
        SettingsActivity.this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SETTING: {
                //设置成功，再次请求更新
                break;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("SettingActivity", "SettingActivity-->onStop()");
        SettingsActivity.this.finish();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        Log.i("SettingActivity", "SettingActivity-->onDestroy()");
        SettingsActivity.this.finish();
    }


}
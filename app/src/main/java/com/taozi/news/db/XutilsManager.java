package com.taozi.news.db;

import android.os.Environment;

import org.xutils.DbManager;
import org.xutils.x;

import java.io.File;

/**
 * Created by Tao Yimin on 2016/10/1.
 * 数据库管理类
 */
public class XutilsManager {
    private DbManager NewsDb;

    private static XutilsManager instance;

    private XutilsManager() {
    }

    public static XutilsManager getInstance() {
        if (instance == null) {
            synchronized (XutilsManager.class) {
                if (instance == null) {
                    instance = new XutilsManager();
                }
            }
        }
        return instance;
    }

    public DbManager getNewsDb() {
        if (NewsDb == null) {
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                return null;
            }
            // 数据库目录
            File file = new File(Environment.getExternalStorageDirectory()+"/News/DB");
            if (!file.exists()) {
                file.mkdirs();
            }
            DbManager.DaoConfig config = new DbManager.DaoConfig()
                    // 设置数据库目录
                    .setDbDir(file)
                    // 设置数据库名称
                    .setDbName("NewsDb.db")
                    // 设置版本号
                    .setDbVersion(1)
                    // 数据库升级的监听
                    .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                           //数据库升级操作
                        }
                    });
            NewsDb = x.getDb(config);
        }
        return NewsDb;
    }
}


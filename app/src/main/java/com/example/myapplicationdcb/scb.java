package com.example.myapplicationdcb;

import android.provider.BaseColumns;

public class scb {
    public scb() {
    }

    public static abstract class scb1 implements BaseColumns {
        public static final String TABLE_NAME="scb1";
        public static final String COLUMN_NAME_WORD="word";//列：单词
        public static final String COLUMN_NAME_MEANING="meaning";//列：单词含义
        public static final String COLUMN_NAME_SAMPLE="sample";//单词示例
    }
}
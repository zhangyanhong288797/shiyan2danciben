package com.example.asus.danciben;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;

import com.example.asus.danciben.wordcontract.Words;


    public class scbActivity extends AppCompatActivity implements
            WordItemFragment.OnFragmentInteractionListener,
            WordDetailFragment.OnFragmentInteractionListener {
        private static final String TAG = "scbActivity";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_scb);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            // getSupportFragmentManager().beginTransaction().add(R.id.container,new WordItemFragment()).commit();
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    insertDialog();
                }
            });
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            WordsDB.getWordsDB().close();
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_book, menu);

            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            switch (id) {
                case R.id.action_search:
                    //查找
                    searchDialog();
                    return true;
                case R.id.action_insert:
                    //新增单词
                    insertDialog();
                    return true;
                default:
                    break;
                case  R.id.action_help:
                    //帮助菜单
                    AlertDialog.Builder dialog = new AlertDialog.Builder(scbActivity.this);
                    dialog.setTitle("This is HELP");
                    dialog.setMessage("HELP");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("Ture", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.setNegativeButton("False", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    dialog.show();
            }

            return super.onOptionsItemSelected(item);
        }

        //更新单词列表

        private void refreshWordItemFragment() {
            WordItemFragment wordItemFragment =
                    (WordItemFragment) getFragmentManager().findFragmentById(R.id.wordslist);
            wordItemFragment.refreshWordsList();
        }

        /**
         * 更新单词列表
         */
        private void refreshWordItemFragment(String strWord) {
            WordItemFragment wordItemFragment =
                    (WordItemFragment) getFragmentManager().findFragmentById(R.id.wordslist);
            wordItemFragment.refreshWordsList(strWord);
        }

        //新增对话框
        private void insertDialog() {
            final TableLayout tableLayout =
                    (TableLayout) getLayoutInflater().inflate(R.layout.insert, null);
            new AlertDialog.Builder(this)
                    .setTitle(R.string.add_word)//标题
                    .setView(tableLayout)//设置视图
                    //确定按钮及其动作
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String strWord =
                                    ((EditText) tableLayout.findViewById(R.id.txtWord)).getText()
                                            .toString();
                            String strMeaning =
                                    ((EditText) tableLayout.findViewById(R.id.txtMeaning)).getText()
                                            .toString();
                            String strSample =
                                    ((EditText) tableLayout.findViewById(R.id.txtSample)).getText()
                                            .toString();

                            //既可以使用Sql语句插入，也可以使用使用insert方法插入
                            // insertUserSql(strWord, strMeaning, strSample);
                            WordsDB.getWordsDB().insert(strWord, strMeaning, strSample);

                            //单词已经插入到数据库，更新显示列表
                            refreshWordItemFragment();

                        }
                    })
                    //取消按钮及其动作
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .create()//创建对话框
                    .show();//显示对话框

        }

        //删除对话框
        private void deleteDialog(final String strId) {
            new AlertDialog.Builder(this).setTitle(R.string.delete_word)
                    .setMessage(R.string.delete_word_confirm)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //既可以使用Sql语句删除，也可以使用使用delete方法删除
                            WordsDB.getWordsDB().deleteUseSql(strId);

                            //单词已经删除，更新显示列表
                            refreshWordItemFragment();
                        }
                    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).create().show();
        }

        //修改对话框
        private void updateDialog(final String strId, final String strWord, final String strMeaning,
                                  final String strSample) {
            final TableLayout tableLayout =
                    (TableLayout) getLayoutInflater().inflate(R.layout.insert, null);
            ((EditText) tableLayout.findViewById(R.id.txtWord)).setText(strWord);
            ((EditText) tableLayout.findViewById(R.id.txtMeaning)).setText(strMeaning);
            ((EditText) tableLayout.findViewById(R.id.txtSample)).setText(strSample);
            new AlertDialog.Builder(this)
                    .setTitle(R.string.modify_word)//标题
                    .setView(tableLayout)//设置视图
                    //确定按钮及其动作
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String strNewWord =
                                    ((EditText) tableLayout.findViewById(R.id.txtWord)).getText()
                                            .toString();
                            String strNewMeaning =
                                    ((EditText) tableLayout.findViewById(R.id.txtMeaning)).getText()
                                            .toString();
                            String strNewSample =
                                    ((EditText) tableLayout.findViewById(R.id.txtSample)).getText()
                                            .toString();

                            //既可以使用Sql语句更新，也可以使用使用update方法更新
                            WordsDB.getWordsDB()
                                    .updateUseSql(strId, strWord, strNewMeaning, strNewSample);

                            //单词已经更新，更新显示列表
                            refreshWordItemFragment();
                        }
                    })
                    //取消按钮及其动作
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .create()//创建对话框
                    .show();//显示对话框

        }

        //查找对话框
        private void searchDialog() {
            final TableLayout tableLayout =
                    (TableLayout) getLayoutInflater().inflate(R.layout.searchterm, null);
            new AlertDialog.Builder(this)
                    .setTitle(R.string.search_word)//标题
                    .setView(tableLayout)//设置视图
                    //确定按钮及其动作
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String txtSearchWord =
                                    ((EditText) tableLayout.findViewById(R.id.txtSearchWord)).getText()
                                            .toString();

                            //单词已经插入到数据库，更新显示列表
                            refreshWordItemFragment(txtSearchWord);
                        }
                    })
                    //取消按钮及其动作
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .create()//创建对话框
                    .show();//显示对话框

        }

        /**
         * 当用户在单词详细Fragment中单击时回调此函数
         */
        @Override
        public void onWordDetailClick(Uri uri) {

        }

        /**
         * 当用户在单词列表Fragment中单击某个单词时回调此函数
         * 判断如果横屏的话，则需要在右侧单词详细Fragment中显示
         */
        @Override
        public void onWordItemClick(String id) {

            if (isLand()) {//横屏的话则在右侧的WordDetailFragment中显示单词详细信息
                changeWordDetailFragment(id);
            } else {
                Intent intent = new Intent(scbActivity.this, WordDetailActivity.class);
                intent.putExtra(WordDetailFragment.ARG_ID, id);
                startActivity(intent);
            }

        }

        //是否是横屏
        private boolean isLand() {
        /*
        Fragment fragment=getFragmentManager().findFragmentById(R.id.worddetail);
        if(fragment==null)
            return false;
        return true;
        */

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                return true;
            }
            return false;
        }

        private void changeWordDetailFragment(String id) {
            Bundle arguments = new Bundle();
            arguments.putString(WordDetailFragment.ARG_ID, id);
            Log.v(TAG, id);

            WordDetailFragment fragment = new WordDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction().replace(R.id.worddetail, fragment).commit();
        }

        @Override
        public void onDeleteDialog(String strId) {
            deleteDialog(strId);
        }

        @Override
        public void onUpdateDialog(String strId) {
            if (strId != null) {
                Words.WordDescription item = WordsDB.getWordsDB().getSingleWord(strId);
                if (item != null) {
                    updateDialog(strId, item.word, item.meaning, item.sample);
                }

            }

        }
    }
package com.example.asus.danciben;

import android.util.Log;

import com.example.asus.danciben.wordcontract.Words;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class ReadWordByYouDao extends Thread {

    private String word;
    private String resultJson;
    private static final String TAG = "ReadWordByYouDao";

    //  public ReadWordByYouDao(){
    //    word = "one";
    // }

    public ReadWordByYouDao(String word) {
        this.word = word;
    }

    @Override
    public void run() {

        try {

            URL url = new URL("http://fanyi.youdao.com/openapi.do");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("encoding", "utf-8");
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            OutputStream out = connection.getOutputStream();
            OutputStreamWriter outWriter = new OutputStreamWriter(out);
            BufferedWriter bufferW = new BufferedWriter(outWriter);

            String require =
                    "keyfrom=wordbookaaaa&key=1303333811&type=data&doctype=json&version=1.1&q=";

            bufferW.write(require + word);
            bufferW.flush();

            InputStream in = connection.getInputStream();
            InputStreamReader inReader = new InputStreamReader(in);
            BufferedReader bufferR = new BufferedReader(inReader);

            String line;
            StringBuilder strBuilder = new StringBuilder();
            while ((line = bufferR.readLine()) != null) {
                strBuilder.append(line);
            }

            out.close();
            outWriter.close();
            bufferW.close();

            in.close();
            inReader.close();
            bufferR.close();

            resultJson = strBuilder.toString();
            Log.i(TAG, "run: " + resultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Words.YouDaoWord getYouDaoWord(String jsonString) throws JSONException {
        //fengzhuangchengJSON  duixiang
        JSONObject jsonObject = new JSONObject(jsonString);
        System.out.println(jsonObject);
        JSONArray jsonArray = jsonObject.getJSONArray("web");
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            //转换为java对象
            map.put(jsonArray.getJSONObject(i).getString("key"),
                    jsonArray.getJSONObject(i).getString("value"));
        }
        return new Words.YouDaoWord(jsonObject.getString("query"),
                jsonObject.getString("translation"), map);
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getResultJson() {
        return resultJson;
    }

    public void setResultJson(String resultJson) {
        this.resultJson = resultJson;
    }
}


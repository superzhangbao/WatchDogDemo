package com.xiaolan.watchdogdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        const val TAG = "MainActivity"
    }

    var filename = "/sys/devices/platform/watchdog/enable"

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_read_watchdog -> {
                if (!DevicesUtil.isRoot()) {
                    Toast.makeText(this,"没有root权限",Toast.LENGTH_SHORT).show()
                    return
                }
                val sb = StringBuilder()
                //打开文件输入流
                val inputStream = FileInputStream(filename)
                val buffer = ByteArray(1024)
                var len = inputStream.read(buffer)
                //读取文件内容
                while (len > 0) {
                    sb.append(String(buffer, 0, len))
                    //继续将数据放到buffer中
                    len = inputStream.read(buffer)
                }
                // 关闭输入流
                inputStream.close()
                btn_read_watchdog.text = sb.toString()
            }
            R.id.btn_close_watchdog -> {
                if (!DevicesUtil.isRoot()) {
                    Toast.makeText(this,"没有root权限",Toast.LENGTH_SHORT).show()
                    return
                }
                val file = File(filename)
                //检查访问权限，如果没有读写权限，进行文件操作，修改文件访问权限
                if (!file.canRead() || !file.canWrite()) {
                    try {
                        Log.e(TAG, "111111111")
                        //通过挂在到linux的方式，修改文件的操作权限
                        val su = Runtime.getRuntime().exec("/system/bin/su")
                        //一般的都是/system/bin/su路径，有的也是/system/xbin/su
                        val cmd = "chmod 777 " + file.absolutePath + "\n" + "exit\n"
                        su.outputStream.write(cmd.toByteArray())
                        if (su.waitFor() != 0 || !file.canRead() || !file.canWrite()) {
                            throw SecurityException()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        throw SecurityException()
                    }
                }
                Log.e(TAG, "2222222")
                val outputStream = FileOutputStream(filename)
                outputStream.write("0".toByteArray())
                outputStream.close()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_read_watchdog.setOnClickListener(this)
        btn_close_watchdog.setOnClickListener(this)
    }
}

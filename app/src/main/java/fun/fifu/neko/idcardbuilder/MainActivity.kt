package `fun`.fifu.neko.idcardbuilder

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.hutool.core.util.IdcardUtil


class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var awa: MainActivity
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        awa = this
        findViewById<Button>(R.id.kaishi).setOnClickListener {
            val textview2 = findViewById<TextView>(R.id.textView2)
            try {
                val geshu = findViewById<EditText>(R.id.geshu)
                val jieguo = findViewById<EditText>(R.id.jieguo)
                fun readGeShu() = geshu.text.toString().toInt()
                MyIDCardUtil.shengChengShuLiang = readGeShu()
                val currentTimeMillis = System.currentTimeMillis()
                val pwp = MyIDCardUtil.makeAIdcard()
                val qwq = MyIDCardUtil.gson.toJson(pwp)
                jieguo.setText(qwq.toCharArray(), 0, qwq.length)
                textview2.text =
                    "耗时 ${System.currentTimeMillis() - currentTimeMillis}毫秒 终于完毕惹qwq\n一共生成了 ${pwp.size}个 来自\n${
                        MyIDCardUtil.nb[pwp.first().substring(0, 6)]
                    }\n的 ${if (IdcardUtil.getAgeByIdCard(pwp.first()) < 18) "未成年" else "成年"} 小伙伴捏~"
            } catch (e: java.lang.Exception) {
                textview2.text = "遇到错误：$e"
            }

        }

        findViewById<Button>(R.id.fcm).setOnClickListener {
            val textview2 = findViewById<TextView>(R.id.textView2)
            try {
                val uri: Uri = Uri.parse("https://www.bing.com/search?q=%E9%98%B2%E6%B2%89%E8%BF%B7%E8%BA%AB%E4%BB%BD%E8%AF%81%E5%A4%A7%E5%85%A8")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            } catch (e: java.lang.Exception) {
                textview2.text = "遇到错误：$e"
            }

        }
    }
}
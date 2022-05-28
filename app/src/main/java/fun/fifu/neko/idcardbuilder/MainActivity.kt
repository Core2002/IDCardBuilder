package `fun`.fifu.neko.idcardbuilder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var awa: MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        awa = this
        findViewById<Button>(R.id.kaishi).setOnClickListener {
            try {
                val geshu = findViewById<EditText>(R.id.geshu)
                val jieguo = findViewById<EditText>(R.id.jieguo)
                fun readGeShu() = geshu.text.toString().toInt()
                MyIDCardUtil.shengChengShuLiang = readGeShu()
                var currentTimeMillis = System.currentTimeMillis()
                val pwp = MyIDCardUtil.makeAIdcard()
                val qwq = MyIDCardUtil.gson.toJson(pwp)
                jieguo.setText(qwq.toCharArray(), 0, qwq.length)
                Toast.makeText(
                    this,
                    "耗时 ${System.currentTimeMillis()-currentTimeMillis}毫秒 终于完毕惹qwq\n一共生成了${pwp.size}个来自${MyIDCardUtil.nb[pwp.first().substring(0, 6)]}的小伙伴捏~",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: java.lang.Exception) {
                Toast.makeText(this, "遇到错误：$e", Toast.LENGTH_SHORT).show()
            }

        }
    }
}
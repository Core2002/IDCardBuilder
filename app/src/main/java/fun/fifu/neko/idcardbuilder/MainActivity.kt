package `fun`.fifu.neko.idcardbuilder

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import cn.hutool.core.util.IdcardUtil


class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var mainActivity: MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainActivity = this
        findViewById<Button>(R.id.refresh_button).setOnClickListener {
            genIDCards(it.context)
        }

        findViewById<Button>(R.id.view_bing_button).setOnClickListener {
            try {
                val uri: Uri =
                    Uri.parse("https://www.bing.com/search?q=%E9%98%B2%E6%B2%89%E8%BF%B7%E8%BA%AB%E4%BB%BD%E8%AF%81%E5%A4%A7%E5%85%A8")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            } catch (_: Exception) {
            }
        }

        genIDCards(this.baseContext)
    }

    private fun genIDCards(context: Context) {
        findViewById<TextView>(R.id.github_url_text_view)
        try {
            val number = findViewById<EditText>(R.id.build_number)
            val resListView = findViewById<ListView>(R.id.res_list_view)
            fun getNumber() = number.text.toString().toInt()
            MyIDCardUtil.buildNumber = getNumber()
            val currentTimeMillis = System.currentTimeMillis()
            val idCardList = MyIDCardUtil.makeAnIDCard()
            val text =
                "耗时 ${System.currentTimeMillis() - currentTimeMillis}毫秒 终于完毕惹qwq\n一共生成了 ${idCardList.size}个 来自\n${
                    MyIDCardUtil.dataMap[idCardList.first().substring(0, 6)]
                }\n的 ${if (IdcardUtil.getAgeByIdCard(idCardList.first()) < 18) "未成年" else "成年"} 小伙伴捏~"
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
            MyIDCardUtil.dataMap[idCardList.first().substring(0, 6)]?.let { it1 -> idCardList.add(0, it1) }
            val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                this,
                android.R.layout.simple_list_item_1,
                idCardList.toList()
            )
            resListView.adapter = adapter
            resListView.onItemLongClickListener =
                AdapterView.OnItemLongClickListener { _, _, i, _ ->
                    setClipboard(context, idCardList[i])
                    Toast.makeText(context, "已复制：${idCardList[i]}", Toast.LENGTH_SHORT).show()
                    true
                }

        } catch (_: Exception) {
        }
    }

    private fun setClipboard(context: Context, text: String) {
        val clipboard =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
    }
}
package `fun`.fifu.neko.idcardbuilder

import android.annotation.SuppressLint
import android.util.Log
import cn.hutool.core.util.IdcardUtil
import com.google.gson.GsonBuilder
import java.lang.reflect.Field
import java.text.SimpleDateFormat
import java.util.*


object MyIDCardUtil {
    val iu = Class.forName("cn.hutool.core.util.IdcardUtil")
    fun getAccField(field: String): Field {
        return iu.getDeclaredField(field).let {
            it.isAccessible = true
            it
        }
    }

    val CHINA_ID_MAX_LENGTH = getAccField("CHINA_ID_MAX_LENGTH").get(null) as Int
    val CHINA_ID_MIN_LENGTH = getAccField("CHINA_ID_MIN_LENGTH").get(null) as Int
    val CITY_CODES = getAccField("CITY_CODES").get(null) as Map<String, String>
    val POWER = getAccField("POWER").get(null)
    val TW_FIRST_CODE = getAccField("TW_FIRST_CODE").get(null) as Map<Character, Int>
    val gson = GsonBuilder().setPrettyPrinting().create()

    var jsonString = String(MainActivity.awa.assets.open("中国行政区划表.json").readBytes())
    var nb: Map<String, String> = gson.fromJson<Map<String, String>>(jsonString, Map::class.java)

    var shengChengShuLiang = 99
    fun makeAIdcard(): MutableList<String> {
        val diQuNum = nb.keys.shuffled().take(1)
        val sfz = diQuNum.first() + randomDate()
        val list = mutableListOf<String>()

        for (i in 0 until 9999) {
            val tmp = sfz + if (i < 1000) String.format("%04d", i) else i
            if (list.size < shengChengShuLiang) if (IdcardUtil.isValidCard18(tmp)) list.add(tmp) else continue else break
        }

        list.shuffle()
        return list
    }

    fun showFields() {
        iu.declaredFields.forEach {
            it.isAccessible = true
            Log.i("qwq", "$it \t (${it.name})")
        }
    }

    val r = Random()

    @SuppressLint("SimpleDateFormat")
    val sdf = SimpleDateFormat("yyyy")
    val currentDate = sdf.format(Date()).toInt()
    private fun randomDate(): String {
        val year: Int = r.nextInt(currentDate - 2016) + 2000
        val month: Int = r.nextInt(12) + 1
        val Day: Int = r.nextInt(30) + 1
        return year.toString() + cp(month) + cp(Day)
    }

    private fun cp(num: Int): String {
        val Num = num.toString() + ""
        return if (Num.length == 1) {
            "0$Num"
        } else {
            Num
        }
    }
}
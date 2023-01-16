package `fun`.fifu.neko.idcardbuilder

import android.annotation.SuppressLint
import android.util.Log
import cn.hutool.core.util.IdcardUtil
import com.google.gson.GsonBuilder
import java.lang.reflect.Field
import java.text.SimpleDateFormat
import java.util.*


object MyIDCardUtil {
    val utilClass = Class.forName("cn.hutool.core.util.IdcardUtil")
    fun getAccField(field: String): Field {
        return utilClass.getDeclaredField(field).let {
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

    var jsonString = String(MainActivity.mainActivity.assets.open("中国行政区划表.json").readBytes())
    var dataMap: Map<String, String> =
        gson.fromJson<Map<String, String>>(jsonString, Map::class.java)

    var buildNumber = 99
    fun makeAnIDCard(): MutableList<String> {
        val randomCityCode = dataMap.keys.shuffled().take(1)
        val randomCityAndDate = randomCityCode.first() + randomDate()
        val resList = mutableListOf<String>()

        for (i in 0 until 9999) {
            val tmp = randomCityAndDate + if (i < 1000) String.format("%04d", i) else i
            if (resList.size < buildNumber) if (IdcardUtil.isValidCard18(tmp)) resList.add(tmp) else continue else break
        }

        resList.shuffle()
        return resList
    }

    fun showFields() {
        utilClass.declaredFields.forEach {
            it.isAccessible = true
            Log.i("qwq", "$it \t (${it.name})")
        }
    }

    val r = Random()

    @SuppressLint("SimpleDateFormat")
    val sdf = SimpleDateFormat("yyyy")
    private val currentDate = sdf.format(Date()).toInt()
    private fun randomDate(): String {
        val year: Int = r.nextInt(currentDate - 2016) + 2000
        val month: Int = r.nextInt(12) + 1
        val day: Int = r.nextInt(30) + 1
        return year.toString() + handleZero(month) + handleZero(day)
    }

    private fun handleZero(num: Int): String {
        val number = num.toString() + ""
        return if (number.length == 1) {
            "0$number"
        } else {
            number
        }
    }
}
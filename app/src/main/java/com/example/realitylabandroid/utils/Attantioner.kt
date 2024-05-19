package com.example.realitylabandroid.utils

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import me.xdrop.fuzzywuzzy.FuzzySearch

object Attantioner {


    //draw red
    fun checkOnWarningWord(text:String):ArrayList<Int>{
        var bad_word_index = ArrayList<Int>()
        val words = text.split(" ")
        val black_words = black_list.split("\n")
        for(i in 0 until words.size){
           val proc = FuzzySearch.extractOne(words[i],black_words)
            if(proc.score>80){
                bad_word_index.add(i)
            }
        }
        return bad_word_index
    }
    //draw orange
    fun checkOnGoodWord(text:String):ArrayList<Int>{
        var bad_word_index = ArrayList<Int>()
        val words = text.split(" ")
        val good_words = good_list.split("\n")
        for(i in 0 until words.size){
            val proc = FuzzySearch.extractOne(words[i], good_words)
            Log.d("Кислый"," "+words[i]+" "+proc)
            if(proc.score<80){
                Log.d("Кислый"," here")

                bad_word_index.add(i)
            }
        }
        return bad_word_index
    }

    fun drawRedYellow(text: String,redI:ArrayList<Int>,yellowI:ArrayList<Int>):SpannableString{
        val spannable = SpannableString(text)
        for(i in yellowI) {
            val word = text.split(" ")[i]
            val startIndex = text.indexOf(word)
            val endIndex = startIndex + word.length
            if(startIndex !=-1){
                spannable.setSpan(
                    ForegroundColorSpan(Color.YELLOW),
                    startIndex,
                    endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        for(i in redI) {
            val word = text.split(" ")[i]
            val startIndex = text.indexOf(word)
            val endIndex = startIndex + word.length
            if(startIndex !=-1){
                spannable.setSpan(
                    ForegroundColorSpan(Color.RED),
                    startIndex,
                    endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

        return spannable
    }
    fun drawYellow(text: String,index:ArrayList<Int>):SpannableString{
        val spannable = SpannableString(text)
        for(i in index) {
            val word = text.split(" ")[i]
            val startIndex = text.indexOf(word)
            val endIndex = startIndex + word.length
            if(startIndex !=-1){
                spannable.setSpan(
                    ForegroundColorSpan(Color.YELLOW),
                    startIndex,
                    endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        return spannable
    }


    val black_list = "Здравствуйте\n" +
            "Привет\n" +
            "Хорошо\n" +
            "Добрый день\n" +
            "Добрый вечер\n" +
            "До свидания\n" +
            "Спасибо\n" +
            "Пожалуйста\n" +
            "Будьте добры"

    val good_list = "диспетчер\n" +
            "гогино\n" +
            "принимаете\n" +
            "первом\n" +
            "3576\n" +
            "н-3?\n" +
            "ч\n" +
            "шеин\n" +
            "проходов\n" +
            "передал\n" +
            "тогда\n" +
            "следую\n" +
            "светофор\n" +
            "107\n" +
            "знойное\n" +
            "№33\n" +
            "в\n" +
            "клименко\n" +
            "9\n" +
            "г\n" +
            "гогино-знойное\n" +
            "минута\n" +
            "от\n" +
            "перегоне\n" +
            "№2120\n" +
            "абзаково\n" +
            "допустимо\n" +
            "трифонов\n" +
            "шагол\n" +
            "приказ\n" +
            "2604\n" +
            "будет\n" +
            "2501\n" +
            "2675\n" +
            "дсп\n" +
            "второго\n" +
            "проедьте\n" +
            "да\n" +
            "ч-1\n" +
            "карабин\n" +
            "сигналу\n" +
            "выполняйте\n" +
            "закрыт\n" +
            "бузинский\n" +
            "телефонограмма\n" +
            "открыт\n" +
            "слушая\n" +
            "приняла\n" +
            "ч-2\n" +
            "пакет\n" +
            "начальное\n" +
            "22-го\n" +
            "н4\n" +
            "перекос\n" +
            "днц\n" +
            "поездов\n" +
            "ч4\n" +
            "проследовать\n" +
            "тау\n" +
            "светофора\n" +
            "выходной\n" +
            "3-ем\n" +
            "нечетный\n" +
            "машинист\n" +
            "синьков\n" +
            "8:34\n" +
            "01\n" +
            "прибытие\n" +
            "остановиться\n" +
            "10-10\n" +
            "понятно\n" +
            "н-3\n" +
            "02.05.2024г\n" +
            "минуты\n" +
            "за\n" +
            "пс\n" +
            "4-ой\n" +
            "волчков\n" +
            "вами\n" +
            "3004\n" +
            "-\n" +
            "первому\n" +
            "-ого\n" +
            "пикет\n" +
            "-о\n" +
            "глошев\n" +
            "4881\n" +
            "33\n" +
            "к\n" +
            "№2804\n" +
            "по\n" +
            "разрешаете\n" +
            "сигнал\n" +
            "нами\n" +
            "номер\n" +
            "разрешаю\n" +
            "бахтинова\n" +
            "профиле\n" +
            "отправления\n" +
            "максимально\n" +
            "пишу\n" +
            "-ой\n" +
            "входной\n" +
            "6-ой\n" +
            "-го\n" +
            "поездам\n" +
            "сможете\n" +
            "гибриткин\n" +
            "отправляюсь\n" +
            "запишите\n" +
            "встали\n" +
            "бускуль\n" +
            "2-й\n" +
            "удобном\n" +
            "отправляейтесь\n" +
            "крацево\n" +
            "урал-тау\n" +
            "поезда\n" +
            "время\n" +
            "3-м\n" +
            "10\n" +
            "не\n" +
            "вплотную\n" +
            "часов\n" +
            "ехать\n" +
            "урал-\n" +
            "перегон\n" +
            "нет\n" +
            "входного\n" +
            "№2604\n" +
            "22-ой\n" +
            "-ий\n" +
            "следуем\n" +
            "тамерлан-тумак\n" +
            "путь\n" +
            "приближении\n" +
            "подтянули\n" +
            "перекрывайте\n" +
            "5\n" +
            "слушает\n" +
            "погромное\n" +
            "после\n" +
            "до\n" +
            "кравцево\n" +
            "60\n" +
            "второй\n" +
            "2804\n" +
            "четного\n" +
            "на\n" +
            "№\n" +
            "слушаю\n" +
            "перекрываю\n" +
            "ч2\n" +
            "с\n" +
            "двадцать\n" +
            "пути\n" +
            "13:20\n" +
            "станцию\n" +
            "петрова\n" +
            "№4881\n" +
            "поезду\n" +
            "предвходного\n" +
            "2-ой\n" +
            "у\n" +
            "буду\n" +
            "остановку\n" +
            "первого\n" +
            "-й\n" +
            "васильев\n" +
            "скорость\n" +
            "безостановочно\n" +
            "02\n" +
            "федоров\n" +
            "ый\n" +
            "час\n" +
            "прибываете\n" +
            "запрещающий\n" +
            "заливное\n" +
            "орланом\n" +
            "сорочинская\n" +
            "2208\n" +
            "бочкарева\n" +
            "сорочинск\n" +
            "станции\n" +
            "нечетных\n" +
            "чиж\n" +
            "4-й\n" +
            "вас\n" +
            "сможем\n" +
            "записываю\n" +
            "принимать\n" +
            "лебяжья-сибирская\n" +
            "тчм\n" +
            "всем\n" +
            "скрещение\n" +
            "допустимой\n" +
            "км\n" +
            "верно\n" +
            "тоцкой\n" +
            "есаульская"
}
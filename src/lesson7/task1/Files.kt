@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson7.task1

import java.io.File
import java.lang.IndexOutOfBoundsException

// Урок 7: работа с файлами
// Урок интегральный, поэтому его задачи имеют сильно увеличенную стоимость
// Максимальное количество баллов = 55
// Рекомендуемое количество баллов = 20
// Вместе с предыдущими уроками (пять лучших, 3-7) = 55/103

/**
 * Пример
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * Вывести его в выходной файл с именем outputName, выровняв по левому краю,
 * чтобы длина каждой строки не превосходила lineLength.
 * Слова в слишком длинных строках следует переносить на следующую строку.
 * Слишком короткие строки следует дополнять словами из следующей строки.
 * Пустые строки во входном файле обозначают конец абзаца,
 * их следует сохранить и в выходном файле
 */
fun alignFile(inputName: String, lineLength: Int, outputName: String) {
    val writer = File(outputName).bufferedWriter()
    var currentLineLength = 0
    fun append(word: String) {
        if (currentLineLength > 0) {
            if (word.length + currentLineLength >= lineLength) {
                writer.newLine()
                currentLineLength = 0
            } else {
                writer.write(" ")
                currentLineLength++
            }
        }
        writer.write(word)
        currentLineLength += word.length
    }
    for (line in File(inputName).readLines()) {
        if (line.isEmpty()) {
            writer.newLine()
            if (currentLineLength > 0) {
                writer.newLine()
                currentLineLength = 0
            }
            continue
        }
        for (word in line.split(Regex("\\s+"))) {
            append(word)
        }
    }
    writer.close()
}

/**
 * Простая (8 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * Некоторые его строки помечены на удаление первым символом _ (подчёркивание).
 * Перенести в выходной файл с именем outputName все строки входного файла, убрав при этом помеченные на удаление.
 * Все остальные строки должны быть перенесены без изменений, включая пустые строки.
 * Подчёркивание в середине и/или в конце строк значения не имеет.
 */
fun deleteMarked(inputName: String, outputName: String) {
    val writer = File(outputName).bufferedWriter()
    for (line in File(inputName).readLines()) {
        if (line.startsWith('_')) continue
        writer.write(line)
        writer.newLine()
    }
    writer.close()
}

/**
 * Средняя (14 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * На вход подаётся список строк substrings.
 * Вернуть ассоциативный массив с числом вхождений каждой из строк в текст.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 */
fun countSubstrings(inputName: String, substrings: List<String>): Map<String, Int> {
    val ans = mutableMapOf<String, Int>()
    for (sub in substrings) {
        ans[sub] =
            File(inputName).readLines()
                .sumOf { line -> "(?=\\$sub)".toRegex(RegexOption.IGNORE_CASE).findAll(line).count() }

    }
    return ans
}


/**
 * Средняя (12 баллов)
 *
 * В русском языке, как правило, после букв Ж, Ч, Ш, Щ пишется И, А, У, а не Ы, Я, Ю.
 * Во входном файле с именем inputName содержится некоторый текст на русском языке.
 * Проверить текст во входном файле на соблюдение данного правила и вывести в выходной
 * файл outputName текст с исправленными ошибками.
 *
 * Регистр заменённых букв следует сохранять.
 *
 * Исключения (жюри, брошюра, парашют) в рамках данного задания обрабатывать не нужно
 *
 */
fun sibilants(inputName: String, outputName: String) {
    val b = mapOf(
        "жы" to "жи",
        "шы" to "ши",
        "чя" to "ча",
        "щя" to "ща",
        "жю" to "жу",
        "чю" to "чу",
        "шю" to "шу",
        "щю" to "щу",
        "жя" to "жа",
        "шя" to "ша",
        "чы" to "чи",
        "щы" to "щи"
    )
    val writer = File(outputName).bufferedWriter()
    val upChar = mutableListOf<Int>()
    for (line in File(inputName).bufferedReader().readLines()) {
        upChar.clear()
        var temp = line
        for (w in temp.indices) {
            if (temp[w].isUpperCase()) {
                upChar.add(w)
            }
        }
        for ((key, value) in b) {
            temp = temp.replace(Regex(key, RegexOption.IGNORE_CASE), value)
        }
        val ans = buildString {
            var i = 0
            for (ind in temp.indices) {
                if (i < upChar.size && ind == upChar[i]) {
                    append(temp[ind].uppercase())
                    i++
                    continue
                }
                append(temp[ind])
            }
        }
        writer.write(ans)
        writer.newLine()
    }

    writer.close()
}

/**
 * Средняя (15 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по центру
 * относительно самой длинной строки.
 *
 * Выравнивание следует производить путём добавления пробелов в начало строки.
 *
 *
 * Следующие правила должны быть выполнены:
 * 1) Пробелы в начале и в конце всех строк не следует сохранять.
 * 2) В случае невозможности выравнивания строго по центру, строка должна быть сдвинута в ЛЕВУЮ сторону
 * 3) Пустые строки не являются особым случаем, их тоже следует выравнивать
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых)
 *
 */
fun centerFile(inputName: String, outputName: String) {
    TODO()
}

/**
 * Сложная (20 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по левому и правому краю относительно
 * самой длинной строки.
 * Выравнивание производить, вставляя дополнительные пробелы между словами: равномерно по всей строке
 *
 * Слова внутри строки отделяются друг от друга одним или более пробелом.
 *
 * Следующие правила должны быть выполнены:
 * 1) Каждая строка входного и выходного файла не должна начинаться или заканчиваться пробелом.
 * 2) Пустые строки или строки из пробелов трансформируются в пустые строки без пробелов.
 * 3) Строки из одного слова выводятся без пробелов.
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых).
 *
 * Равномерность определяется следующими формальными правилами:
 * 5) Число пробелов между каждыми двумя парами соседних слов не должно отличаться более, чем на 1.
 * 6) Число пробелов между более левой парой соседних слов должно быть больше или равно числу пробелов
 *    между более правой парой соседних слов.
 *
 * Следует учесть, что входной файл может содержать последовательности из нескольких пробелов  между слвоами. Такие
 * последовательности следует учитывать при выравнивании и при необходимости избавляться от лишних пробелов.
 * Из этого следуют следующие правила:
 * 7) В самой длинной строке каждая пара соседних слов должна быть отделена В ТОЧНОСТИ одним пробелом
 * 8) Если входной файл удовлетворяет требованиям 1-7, то он должен быть в точности идентичен выходному файлу
 */
fun alignFileByWidth(inputName: String, outputName: String) {
    TODO()
}

/**
 * Средняя (14 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * Вернуть ассоциативный массив, содержащий 20 наиболее часто встречающихся слов с их количеством.
 * Если в тексте менее 20 различных слов, вернуть все слова.
 * Вернуть ассоциативный массив с числом слов больше 20, если 20-е, 21-е, ..., последнее слова
 * имеют одинаковое количество вхождений (см. также тест файла input/onegin.txt).
 *
 * Словом считается непрерывная последовательность из букв (кириллических,
 * либо латинских, без знаков препинания и цифр).
 * Цифры, пробелы, знаки препинания считаются разделителями слов:
 * Привет, привет42, привет!!! -привет?!
 * ^ В этой строчке слово привет встречается 4 раза.
 *
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 * Ключи в ассоциативном массиве должны быть в нижнем регистре.
 *
 */
fun top20Words(inputName: String): Map<String, Int> = TODO()

/**
 * Средняя (14 баллов)
 *
 * Реализовать транслитерацию текста из входного файла в выходной файл посредством динамически задаваемых правил.

 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * В ассоциативном массиве dictionary содержится словарь, в котором некоторым символам
 * ставится в соответствие строчка из символов, например
 * mapOf('з' to "zz", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "yy", '!' to "!!!")
 *
 * Необходимо вывести в итоговый файл с именем outputName
 * содержимое текста с заменой всех символов из словаря на соответствующие им строки.
 *
 * При этом регистр символов в словаре должен игнорироваться,
 * но при выводе символ в верхнем регистре отображается в строку, начинающуюся с символа в верхнем регистре.
 *
 * Пример.
 * Входной текст: Здравствуй, мир!
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Пример 2.
 *
 * Входной текст: Здравствуй, мир!
 * Словарь: mapOf('з' to "zZ", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "YY", '!' to "!!!")
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun transliterate(inputName: String, dictionary: Map<Char, String>, outputName: String) {
    TODO()
}

/**
 * Средняя (12 баллов)
 *
 * Во входном файле с именем inputName имеется словарь с одним словом в каждой строчке.
 * Выбрать из данного словаря наиболее длинное слово,
 * в котором все буквы разные, например: Неряшливость, Четырёхдюймовка.
 * Вывести его в выходной файл с именем outputName.
 * Если во входном файле имеется несколько слов с одинаковой длиной, в которых все буквы разные,
 * в выходной файл следует вывести их все через запятую.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 * Пример входного файла:
 * Карминовый
 * Боязливый
 * Некрасивый
 * Остроумный
 * БелогЛазый
 * ФиолетОвый

 * Соответствующий выходной файл:
 * Карминовый, Некрасивый
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun chooseLongestChaoticWord(inputName: String, outputName: String) {
    TODO()
}

/**
 * Сложная (22 балла)
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе элементы текстовой разметки следующих типов:
 * - *текст в курсивном начертании* -- курсив
 * - **текст в полужирном начертании** -- полужирный
 * - ~~зачёркнутый текст~~ -- зачёркивание
 *
 * Следует вывести в выходной файл этот же текст в формате HTML:
 * - <i>текст в курсивном начертании</i>
 * - <b>текст в полужирном начертании</b>
 * - <s>зачёркнутый текст</s>
 *
 * Кроме того, все абзацы исходного текста, отделённые друг от друга пустыми строками, следует обернуть в теги <p>...</p>,
 * а весь текст целиком в теги <html><body>...</body></html>.
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 * Отдельно следует заметить, что открывающая последовательность из трёх звёздочек (***) должна трактоваться как "<b><i>"
 * и никак иначе.
 *
 * При решении этой и двух следующих задач полезно прочитать статью Википедии "Стек".
 *
 * Пример входного файла:
Lorem ipsum *dolor sit amet*, consectetur **adipiscing** elit.
Vestibulum lobortis, ~~Est vehicula rutrum *suscipit*~~, ipsum ~~lib~~ero *placerat **tortor***,

Suspendisse ~~et elit in enim tempus iaculis~~.
 *
 * Соответствующий выходной файл:
<html>
<body>
<p>
Lorem ipsum <i>dolor sit amet</i>, consectetur <b>adipiscing</b> elit.
Vestibulum lobortis. <s>Est vehicula rutrum <i>suscipit</i></s>, ipsum <s>lib</s>ero <i>placerat <b>tortor</b></i>.
</p>
<p>
Suspendisse <s>et elit in enim tempus iaculis</s>.
</p>
</body>
</html>
 *
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */
fun markdownToHtmlSimple(inputName: String, outputName: String) {
    TODO()
}

/**
 * Сложная (23 балла)
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе набор вложенных друг в друга списков.
 * Списки бывают двух типов: нумерованные и ненумерованные.
 *
 * Каждый элемент ненумерованного списка начинается с новой строки и символа '*', каждый элемент нумерованного списка --
 * с новой строки, числа и точки. Каждый элемент вложенного списка начинается с отступа из пробелов, на 4 пробела большего,
 * чем список-родитель. Максимально глубина вложенности списков может достигать 6. "Верхние" списки файла начинются
 * прямо с начала строки.
 *
 * Следует вывести этот же текст в выходной файл в формате HTML:
 * Нумерованный список:
 * <ol>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ol>
 *
 * Ненумерованный список:
 * <ul>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ul>
 *
 * Кроме того, весь текст целиком следует обернуть в теги <html><body><p>...</p></body></html>
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 *
 * Пример входного файла:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
 * Утка по-пекински
 * Утка
 * Соус
 * Салат Оливье
1. Мясо
 * Или колбаса
2. Майонез
3. Картофель
4. Что-то там ещё
 * Помидоры
 * Фрукты
1. Бананы
23. Яблоки
1. Красные
2. Зелёные
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 *
 *
 * Соответствующий выходной файл:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
<html>
<body>
<p>
<ul>
<li>
Утка по-пекински
<ul>
<li>Утка</li>
<li>Соус</li>
</ul>
</li>
<li>
Салат Оливье
<ol>
<li>Мясо
<ul>
<li>Или колбаса</li>
</ul>
</li>
<li>Майонез</li>
<li>Картофель</li>
<li>Что-то там ещё</li>
</ol>
</li>
<li>Помидоры</li>
<li>Фрукты
<ol>
<li>Бананы</li>
<li>Яблоки
<ol>
<li>Красные</li>
<li>Зелёные</li>
</ol>
</li>
</ol>
</li>
</ul>
</p>
</body>
</html>
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */

//fun helpFun(i: Int, fileList: List<String>): Pair<Int, StringBuilder> {
//
//    var ans = StringBuilder()
//    var temp = 0
//    if (fileList[i].startsWith('*')) {
//        ans = StringBuilder().appendHTML().li {
//            for (lineInd in i until fileList.size) {
//                if (fileList[lineInd].startsWith('*')) {
//                    temp = lineInd
//                    +fileList[lineInd].substring(1)
//                    break
//                }
//            }
//        }
//    }
//    return Pair(temp, ans)
//}

fun markdownToHtmlLists(inputName: String, outputName: String) {
    TODO()
//    val file = File(inputName).bufferedReader().readLines()
//    val ans = StringBuilder().appendHTML().html {
//        body {
//            p {
//                if (file[0].startsWith('*')) {
//                    ul {
//                        var lineInd = 0
//                        while (lineInd < file.size) {
//                            lineInd = helpFun(lineInd, file).first
//                            +helpFun(lineInd, file).second.toString()
//                            lineInd++
//                        }
//                    }
//                } else {
//                    ol {
//                        var lineInd = 0
//                        while (lineInd < file.size) {
//                            lineInd = helpFun(lineInd, file).first
//                            +helpFun(lineInd, file).second.toString()
//                            lineInd++
//                        }
//                    }
//                }
//            }
//        }
//    }.toString()
//    val writer = File(outputName).bufferedWriter()
//    writer.write(ans)
//    writer.close()
}

/**
 * Очень сложная (30 баллов)
 *
 * Реализовать преобразования из двух предыдущих задач одновременно над одним и тем же файлом.
 * Следует помнить, что:
 * - Списки, отделённые друг от друга пустой строкой, являются разными и должны оказаться в разных параграфах выходного файла.
 *
 */
fun markdownToHtml(inputName: String, outputName: String) {
    TODO()
}

/**
 * Средняя (12 баллов)
 *
 * Вывести в выходной файл процесс умножения столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 111):
19935
 *    111
--------
19935
+ 19935
+19935
--------
2212785
 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 * Нули в множителе обрабатывать так же, как и остальные цифры:
235
 *  10
-----
0
+235
-----
2350
 *
 */
fun printMultiplicationProcess(lhv: Int, rhv: Int, outputName: String) {
    TODO()
}


/**
 * Сложная (25 баллов)
 *
 * Вывести в выходной файл процесс деления столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 22):
19935 | 22
-198     906
----
13
-0
--
135
-132
----
3

 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 *
 */
fun printDivisionProcess(lhv: Int, rhv: Int, outputName: String) {
    val writer = File(outputName).bufferedWriter()
    val list = mutableListOf<Int>()
    var ost = lhv % rhv
    var ans = lhv / rhv
    var temp: Int
    var count = 0
    var s = 0
    if (rhv > lhv) {
        list.add(0)
    }
    list.add(ost)
    while (ans > 0) {
        temp = rhv * (ans % 10)
        list.add(temp)
        list.add(temp + ost)
        ost = temp / 10
        ans /= 10
    }
    count = list[list.lastIndex].toString().length
    list.removeAt(list.lastIndex)
    if (count > list[list.lastIndex].toString().length) {
        writer.write("$lhv | $rhv")
        s++
    } else {
        writer.write(" $lhv | $rhv")
    }
    writer.newLine()
    writer.write("-" + list[list.lastIndex].toString())
    while(s + list.last().toString().length != lhv.toString().length){
        writer.write(" ")
        s++
    }
    s = 9
    writer.write("   " + (lhv / rhv).toString())
    var t = 0
    writer.newLine()
    while (t <= list[list.lastIndex].toString().length) {
        writer.write("-")
        t++
        s++
    }
    count = t
    var buf = 0
    for (i in list.lastIndex - 1 downTo 1 step 2) {
//        writer.newLine()
//        p = 0
//        while (p <= count) {
//            writer.write(" ")
//            p++
//        }
//        if (list[i].toString().length == 1) {
//            writer.write("0" + list[i].toString())
//            if (list[i - 1].toString().length == list[i + 1].toString().length + 1) {
//                count--
//            }
//        } else {
//            writer.write(list[i].toString())
//            if (list[i - 1].toString().length == list[i].toString().length) {
//                count--
//            }
//        }
//        writer.newLine()
//        p = 0
//        while (p <= count) {
//            writer.write(" ")
//            p++
//        }
//        writer.write("-" + list[i - 1].toString())
//        writer.newLine()
//        p = 0
//        s = 0
//        while (p <= count) {
//            writer.write(" ")
//            p++
//            s++
//        }
//        var t = 0
//        while (t <= list[i - 1].toString().length) {
//            writer.write("-")
//            t++
//            s++
//        }
//        if (list[i - 1].toString().length == 1) {
//            if ((list[i].toString().length + 1 == list[i - 2].toString().length) && (list[i].toString().length != list[i - 1].toString().length)) {
//                println(i)
//                count--
//            }
//        }
//        count += (t-1)
//        print(count)
        buf = 0
        var t = 0
        writer.newLine()
        if (list[i].toString().length == 1) t++ //t++ приближает конец цикла так как на 0 больше
        while (t + list[i].toString().length != count + 1) {
            writer.write(" ")
            t++
        }
        if (list[i].toString().length == 1) {
            writer.write("0")
        }
        writer.write(list[i].toString())
        t = 0
        writer.newLine()
        if (list[i].toString().length > list[i - 1].toString().length) t--
        while (t + list[i].toString().length != count) {
            writer.write(" ")
            t++
        }
        writer.write("-" + list[i - 1].toString())
        writer.newLine()
        t = 0
        if (list[i].toString().length > list[i - 1].toString().length) t--
        while (t + list[i].toString().length != count) {
            writer.write(" ")
            t++
            buf++
        }
        t = 0
        while (t <= list[i - 1].toString().length) {
            writer.write("-")
            t++
            buf++
        }
        count += (t - 1)
    }
    println(count)
    writer.newLine()
    println(list)
    if (buf == 0) buf = t
    t = 0
    while (t + (lhv % rhv).toString().length != buf) {
        writer.write(" ")
        t++
    }
    writer.write((lhv % rhv).toString())
    writer.close()
}

/**
 * На вход подается ассоциативный массив carPetrols, в котором
 * указано какой тип топлива необходим для указанных моделей
 * автомобилей. Пример:
 * Lada Vesta - бензин 98
 * Lada Niva - дизель
 * BMW M5 - бензин 95
 * Копейка - бензин 88
 * Трактор - солярка
 *
 * На вход также подается строка gasStations, которая содержит
 * информацию о доступных заправках в следующем формате:
 * *Название заправки*: *вид топлива* - *цена*; *вид топлива* - *цена*; ...
 *
 * Пример:
 * Лукойл: бензин 95 - 44.66; дизель - 60.76; солярка - 10;
 * Газпром: бензин 98 - 50.00; бензин 88 - 34.30;
 * Shell: бензин 66 - 23.00; дизель - 55.50;
 *
 * Заправки отделены друг от друга переносами строк
 *
 * Необходимо для каждой марки автомобиля из carPetrols найти
 * наиболее выгодную заправку. Если ни одна из доступных заправок
 * не продает топливо необходимого вида следует выбросить
 * IllegalStateException.
 *
 * Для приведенного примера ответ должен быть следующим:
 * Lada Vesta - Газпром
 * Lada Niva - Shell
 * BMW M5 - Лукойл
 * Копейка - Газпром
 * Трактор - Лукойл
 *
 * При нарушении формата входных данных следует выбросить IllegalArgumentException.
 *
 * Имя функции и тип результата функции предложить самостоятельно;
 * в задании указан тип Collection<Any>, то есть коллекция объектов
 * произвольного типа, можно (и нужно) изменить как вид коллекции,
 * так и тип её элементов.
 *
 * Кроме функции, следует написать тесты,
 * подтверждающие её работоспособность.
 */
fun test(carPetrols: Map<String, String>, gasStations: String): Map<String, String> {
    val stations = gasStations.split("\n")
    val ans = mutableMapOf<String, Double>()
    val ans2 = mutableMapOf<String, String>()
    var temp: String
    var name: String
    var price: Double
    for (i in stations) {
        if (!i.contains(
                Regex(
                    """ [а-яёa-z]+:( [а-яёa-z]+ [0-9]+ - \d(\.\d+)?;(\n)?)+""",
                    RegexOption.IGNORE_CASE
                )
            )
        ) {//доделать
            throw IllegalArgumentException()
        }
    }
    for ((key, value) in carPetrols) {
        for (i in stations) {
            temp = Regex("""$value - [0-9]+\.[0-9]+""", RegexOption.IGNORE_CASE).find(i)?.value ?: ""
            price = (Regex("""[0-9]+\.[0-9]+""", RegexOption.IGNORE_CASE).find(temp)?.value
                ?: throw IllegalArgumentException()).toDouble()
            name = Regex("""([а-яё]+)""", RegexOption.IGNORE_CASE).find(i)?.value ?: throw IllegalArgumentException()
            if (ans[key] == null) {
                ans[key] = price
                ans2[key] = name
            }
            if (ans[key] != null && ans[key]!! > price) {
                ans[key] = price
                ans2[key] = name
            }
        }
    }
    for ((key, value) in ans2) {
        if (value == "") {
            throw IllegalStateException()
        }
    }
    return ans2
}


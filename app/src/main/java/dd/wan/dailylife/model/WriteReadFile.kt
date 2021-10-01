package dd.wan.dailylife.model

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import java.io.File
import java.io.FileWriter
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WriteReadFile(var context: Context) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun writeFile(list: ArrayList<Note>) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val filePaths: String = context.filesDir.parentFile.toString() + "/diary.csv"
        try {
            val file = File(filePaths)
            val fw = FileWriter(file)
            fw.append("Date,Title,Content\n")
            list.forEach {
                fw.append(sdf.format(it.date))
                fw.append(",")
                if (it.title.contains("\n") || it.title.contains(',')) {
                    if (it.title.contains('"')) {
                        var string: ArrayList<String> = it.title.split("\"") as ArrayList<String>
                        string.forEach { item ->
                            if (item != "") {
                                fw.append("\"")
                                fw.append(item)
                                fw.append("\"")
                            }
                        }
                    } else {
                        Log.d("okeeee", "check k được")
                        fw.append("\"")
                        fw.append(it.title)
                        fw.append("\"")
                    }
                } else {
                    if (it.title.contains("\"")) {
                        var string: ArrayList<String> = it.title.split("\"") as ArrayList<String>
                        string.forEach { item ->
                            if (item != "") {
                                fw.append("\"")
                                fw.append(item)
                                fw.append("\"")
                            }
                        }
                    } else {
                        fw.append(it.title)
                    }
                }

                fw.append(",")
                if (it.string.contains("\n") || it.string.contains(',')) {
                    if (it.string.contains('"')) {
                        Log.d("okeeee", "check được")
                        var string: ArrayList<String> = it.string.split("\"") as ArrayList<String>
                        string.forEach { item ->
                            if (item != "") {
                                fw.append("\"")
                                fw.append(item)
                                fw.append("\"")
                            }
                        }
                    } else {
                        Log.d("okeeee", "check k được")
                        fw.append("\"")
                        fw.append(it.string)
                        fw.append("\"")
                    }
                } else {
                    if (it.string.contains("\"")) {
                        var string: ArrayList<String> = it.string.split("\"") as ArrayList<String>
                        string.forEach { item ->
                            if (item != "") {
                                fw.append("\"")
                                fw.append(item)
                                fw.append("\"")
                            }
                        }
                    } else {
                        fw.append(it.string)
                    }
                }
                fw.append("\n")
            }
            fw.flush()
            fw.close()
//             dùng csvPrinter xem dữ liệu lưu như thế nào
//            val writer = Files.newBufferedWriter(Paths.get(file.toURI()))
//            val csvPrinter =
//                CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Date", "Title", "Content"))
//            for (item in list) {
//                csvPrinter.printRecord(sdf.format(item.date), item.title, item.string)
//            }
//            csvPrinter.flush()
//            csvPrinter.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun readFile(): ArrayList<Note> {
        var list = ArrayList<Note>()
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val filePaths: String = context.filesDir.parentFile.toString() + "/diary.csv"
        val file = File(filePaths)
        if (file.exists())
            try {
//                 Vẫn đang nghĩ anh ạ =((
//                var fr = FileReader(file)
//                var bf = BufferedReader(fr)
//                while (true) {
//                    var line: String? = bf.readLine() ?: break
//                    if (!line!!.contains("\"")) // trường hợp không có kí tự xuống dòng hoặc dấu ,
//                    {
//                        var listString: ArrayList<String> = line.split(",") as ArrayList<String>
//                        var date = sdf.parse(listString[0])
//                        list.add(Note(date, listString[1], listString[2]))
//                    } else // trường hợp này thì chắc chắn có thêm dấu phẩy hoặc xuống dòng
//                    {
//                        var listString: ArrayList<String> = line.split(",") as ArrayList<String>
//                        var date = sdf.parse(listString[0])
//                        if (listString[1].contains("\"")) {
//
//                        }
//                    }
//                }


//                 đọc dữ liệu bằng csv
                val reader = Files.newBufferedReader(Paths.get(file.toURI()))
                val records: Iterable<CSVRecord> = CSVFormat.EXCEL.withHeader().parse(reader)
                for (item in records) {
                    val date = sdf.parse(item.get("Date"))
                    val title = item.get("Title")
                    val content = item.get("Content")
                    list.add(Note(date, title, content))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        return list
    }
}
package dd.wan.dailylife.model

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.apache.commons.csv.CSVRecord
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
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
            fw.append("Date,Title,Content\n") // add header

            // Bắt đầu cuộc hành trình nào
            list.forEach {
                fw.append(sdf.format(it.date)) //  thêm ngày trước
                fw.append(",")
                if (it.title.contains("\n") || it.title.contains(',')) {            // tìm xem có  tồn tại trường hợp đặc biệt không
                    if (it.title.contains('"')) {                                       // trường hợp vô cùng đặc biệt
                        fw.append("\"")                                                         // kiểu gì cũng phải có cái đầu tiên ( có mở đầu và kết thúc )
                        var stringP = ""
                        for (i in it.title.indices) {                                           // check từng kí tự thôi =((
                            if (it.title[i] == '"')                                                 // gặp cái này thì double nó lên
                                stringP += "\"\""
                            else
                                stringP += it.title[i]
                        }
                        fw.append(stringP)
                        fw.append("\"")                                                         // kết thúc nhé
                    } else {                                                                    //
                        fw.append("\"")
                        fw.append(it.title)
                        fw.append("\"")
                    }
                } else {
                    if (it.title.contains('"')) {
                        fw.append("\"")
                        var stringP = ""
                        for (i in it.title.indices) {
                            if (it.title[i] == '"')
                                stringP += "\"\""
                            else
                                stringP += it.title[i]
                        }
                        fw.append(stringP)
                        fw.append("\"")
                    } else {
                        fw.append(it.title)
                    }
                }

                fw.append(",")
                if (it.string.contains("\n") || it.string.contains(',')) { // cái này cũng như cái trên
                    if (it.string.contains('"')) {
                        fw.append("\"")
                        var stringP = ""
                        for (i in 0 until it.string.length) {
                            if (it.string[i] == '"')
                                stringP += "\"\""
                            else
                                stringP += it.string[i]
                        }
                        fw.append(stringP)
                        fw.append("\"")
                    } else {
                        fw.append("\"")
                        fw.append(it.string)
                        fw.append("\"")
                    }
                } else {
                    if (it.string.contains('"')) {
                        fw.append("\"")
                        var stringP = ""
                        for (i in it.string.indices) {
                            if (it.string[i] == '"')
                                stringP += "\"\""
                            else
                                stringP += it.string[i]
                        }
                        fw.append(stringP)
                        fw.append("\"")
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
                var fr = FileReader(file)
                var bf = BufferedReader(fr)
                bf.readLine()
                var dem = 0        // biến này để quy định lấy cột nào 0 -> date , 1->title , 2-> content
                var date = ""
                var title = ""
                var content = ""
                var check = 2     // check xem dòng đó đã đủ hay chưa ( dấu ngoặc kép luôn luôn là số chẵn )
                while (true) {              // let's do it
                    var line: String? = bf.readLine() ?: break
                    if (line!!.contains('"') || check %2!=0)  // đặc biệt hoặc dấu ngoặc kép chưa đủ ( nội dung nhảy xuống dòng )
                    {
                        for (i in line.indices) {
                            when (dem) {
                                0 -> {
                                    if (line[i] == ',' && check % 2 == 0) {    // xem hết cột chưa hết thì chuyển sang tìm title ( dem=1 )
                                        dem = 1
                                        check = 2
                                        continue
                                    }
                                    if (line[i] != ',') {           // chưa tới thì tiếp tục chạy
                                        date += line[i]
                                        continue
                                    }
                                }
                                // 13/10/2021,"c "" hi """,tr
                                1 -> {
                                    if (line[i] == '"') {               // tăng biến check khi thấy kí tự "
                                        check++
                                    }
                                    if (line[i] != ',' || (line[i] == ',' && check % 2 != 0)) {     // nếu k gặp dấu phẩy hoặc gặp nhưng kí tự " không chẵn thì thêm vào title
                                        title += line[i]
                                    }
                                    if (check % 2 == 0 && line[i] == ',') {             // kí tự " chẵn là tới dấu , chuyển sang giai đoạn tìm content
                                        dem = 2
                                        check = 2           // reset check
                                        continue
                                    }
                                }
                                2 -> {
                                    content += line[i]
                                    if (line[i] == '"') {   // tăng biến check khi thấy kí tự "
                                        check++
                                    }
                                    if (check % 2 == 0 && i == line.length - 1) {    // kí tự " chẵn và hết dòng -> tìm xong
                                        check = 2
                                        dem = 0             // quay lại tìm date
                                        break
                                    }
                                    if (i == line.length - 1 && check % 2 != 0) {   // hết dòng nhưng kí tự " không chẵn -> làm tiếp
                                        content += "\n"         //vì k set lại dem nên nó vẫn tiếp tục tìm content
                                        break
                                    }
                                }
                            }
                        }
                        if (check % 2 == 0) {   // check xem content đã đủ chưa
                            if(title.contains("\""))
                            {
                                title = title.substring(1,title.length-1)
                                title = title.replace("\"\"","\"")
                            }
                            if(content.contains("\""))
                            {
                                content = content.substring(1,content.length-1)
                                content = content.replace("\"\"","\"")
                            }
                            var note = Note(sdf.parse(date), title, content)
                            date = ""
                            title = ""
                            content = ""
                            list.add(note)
                        }
                    }
                    else    // trường hợp không có kí tự xuống dòng hoặc dấu , " hoặc là chuỗi content của dòng trên
                    {
                        if(check %2==0)
                        {
                            var listString = line.split(',')
                            var date: Date = sdf.parse(listString[0]) as Date
                            list.add(Note(date, listString[1], listString[2]))
                        }
                    }
                }

//                 đọc dữ liệu bằng csv cái này e cũng test nè hehe
//                val reader = Files.newBufferedReader(Paths.get(file.toURI()))
//                val records: Iterable<CSVRecord> = CSVFormat.EXCEL.withHeader().parse(reader)
//                for (item in records) {
//                    val date = sdf.parse(item.get("Date"))
//                    val title = item.get("Title")
//                    val content = item.get("Content")
//                    list.add(Note(date, title, content))
//                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        return list
    }
}
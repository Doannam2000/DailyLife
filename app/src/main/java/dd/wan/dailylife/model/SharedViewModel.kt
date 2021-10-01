package dd.wan.dailylife.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.collections.ArrayList

class SharedViewModel:ViewModel() {
    private var data = MutableLiveData(ArrayList<Note>())
    private var data2 = MutableLiveData(Calendar.getInstance().time)
    val dataviewModel: LiveData<ArrayList<Note>> = data
    val dataviewModel2: LiveData<Date> = data2
    fun saveData(list : ArrayList<Note>)
    {
        data.value = list
    }
    fun saveData2(date : Date)
    {
        data2.value = date
    }
}
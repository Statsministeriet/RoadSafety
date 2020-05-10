package dk.itu.antj.bachelor.roadsafety.user

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import dk.itu.antj.bachelor.roadsafety.R
import dk.itu.antj.bachelor.roadsafety.db.User

class UserAdapter(private val context: Context,
                  private val dataSource: List<User>):BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    //1
    override fun getCount(): Int {
        return dataSource.size
    }

    //2
    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    //3
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //4
    @SuppressLint("SetTextI18n", "ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val user = getItem(position) as User
        // Get view for row item
        val rowView = inflater.inflate(R.layout.user_list_item, parent, false)
        val name = rowView.findViewById(R.id.list_user_name) as TextView
        name.text = user.firstName
        return rowView
    }
}
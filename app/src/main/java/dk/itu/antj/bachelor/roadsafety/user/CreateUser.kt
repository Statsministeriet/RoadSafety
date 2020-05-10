package dk.itu.antj.bachelor.roadsafety.user

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import dk.itu.antj.bachelor.roadsafety.R
import kotlinx.android.synthetic.main.create_user_fragment.*

class CreateUser : Fragment() {

    companion object {
        fun newInstance() = CreateUser()
    }

    private lateinit var viewModel: CreateUserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_user_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CreateUserViewModel::class.java)
        create_user_button.setOnClickListener {

            try {
                viewModel.createNewUser(
                    firstName = first_name_edit.text.toString(),
                    lastName = last_name_edit.text.toString(),
                    birthYear = birthday_edit.year,
                    birthMonth = birthday_edit.month,
                    birthDay = birthday_edit.dayOfMonth,
                    genderFemale = female_radio_edit.isChecked,
                    genderMale = male_radio_edit.isChecked,
                    context = context,
                    bundle = savedInstanceState
                )
            }catch (e: UserException){
                Toast.makeText(context, e.message,Toast.LENGTH_LONG).show()
            }
        }
    }

}

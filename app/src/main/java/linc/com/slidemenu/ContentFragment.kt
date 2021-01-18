package linc.com.slidemenu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.fragment_content.*


class ContentFragment : Fragment(R.layout.fragment_content) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        /*data_tv.setOnClickListener {
//            NavHostFragment.findNavController(this).navigate(R.id.fragment2)
//            Navigation.findNavController(view).navigate(R.id.fragment2)
            Navigation.createNavigateOnClickListener(R.id.action_contentFragment_to_fragment2).onClick(view)
        }*/
        data_tv.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_contentFragment_to_fragment2))


    }

}
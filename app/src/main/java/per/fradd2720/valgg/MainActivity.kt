package per.fradd2720.valgg

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import per.fradd2720.valgg.databinding.ActivityMainBinding
import per.fradd2720.valgg.fragment.AgentsFragment
import per.fradd2720.valgg.fragment.StatusFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val fragmentStatus by lazy { StatusFragment() }
    private val fragmentAgents by lazy { AgentsFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainNavBar.run {
            setOnItemSelectedListener { it ->
                val transaction = supportFragmentManager.beginTransaction()
                supportFragmentManager.fragments.forEach { transaction.hide(it) }

                when (it.itemId) {
                    R.id.nav_status -> {
                        if (supportFragmentManager.findFragmentByTag("0") == null) transaction.add(R.id.mainFrameLayout, fragmentStatus, "0")
                        transaction.show(fragmentStatus)
                    }
                    R.id.nav_ranking -> {
//                        if (supportFragmentManager.findFragmentByTag("1") == null) transaction.add(R.id.mainFrameLayout, fragmentStatus, "1")
//                        transaction.show(fragmentStatus).hide(fragmentAgents)
                    }
                    R.id.nav_agents -> {
                        if (supportFragmentManager.findFragmentByTag("2") == null) transaction.add(R.id.mainFrameLayout, fragmentAgents, "2")
                        transaction.show(fragmentAgents)
                    }
                    R.id.nav_contents -> {
//                        if (supportFragmentManager.findFragmentByTag("3") == null) transaction.add(R.id.mainFrameLayout, fragmentStatus, "3")
//                        transaction.show(fragmentStatus).hide(fragmentAgents)
                    }
                }
                transaction.commit()
                true
            }
            binding.mainNavBar.selectedItemId = R.id.nav_status
        }
    }
}
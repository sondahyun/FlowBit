package com.example.flowbit.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flowbit.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // 버튼 클릭 이벤트 설정
        setupButtons()

        // 탭 전환 기능 설정
        setupTabs()

        return binding.root
    }

    private fun setupButtons() {
        // 받기 버튼
        binding.receiveButton.setOnClickListener {
            startActivity(Intent(requireContext(), ReceiveActivity::class.java))
        }

        // 보내기 버튼
        binding.sendButton.setOnClickListener {
            startActivity(Intent(requireContext(), SendActivity::class.java))
        }

        // 스테이킹 버튼
        binding.stakingButton.setOnClickListener {
            startActivity(Intent(requireContext(), StakingActivity::class.java))
        }

        // 스왑 버튼
        binding.swapButton.setOnClickListener {
            startActivity(Intent(requireContext(), SwapActivity::class.java))
        }
    }

    private fun setupTabs() {
        binding.tabLayout.addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                when (tab?.text) {
                    "크립토" -> loadCryptoData()
                    "NFT" -> loadNftData()
                }
            }

            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
        })
    }

    private fun loadCryptoData() {
        val cryptoList = fetchCryptoList()
        if (cryptoList.isEmpty()) {
            showEmptyState("크립토가 존재하지 않아요!")
        } else {
            hideEmptyState()
            // 크립토 데이터를 RecyclerView에 갱신 (여기서 Adapter 사용)
            // 예: adapter.updateData(cryptoList)
        }
    }

    private fun loadNftData() {
        val nftList = fetchNftList()
        if (nftList.isEmpty()) {
            showEmptyState("NFT가 존재하지 않아요!")
        } else {
            hideEmptyState()
            // NFT 데이터를 RecyclerView에 갱신 (여기서 Adapter 사용)
            // 예: adapter.updateData(nftList)
        }
    }

    private fun showEmptyState(message: String) {
        binding.emptyStateIcon.visibility = View.VISIBLE
        binding.emptyStateText.text = message
        binding.emptyStateText.visibility = View.VISIBLE
    }

    private fun hideEmptyState() {
        binding.emptyStateIcon.visibility = View.GONE
        binding.emptyStateText.visibility = View.GONE
    }

    private fun fetchCryptoList(): List<String> {
        // 크립토 데이터를 가져오는 로직 (현재는 더미 데이터)
        return emptyList() // 빈 리스트로 설정 (데이터가 없을 때)
    }

    private fun fetchNftList(): List<String> {
        // NFT 데이터를 가져오는 로직 (현재는 더미 데이터)
        return emptyList() // 빈 리스트로 설정 (데이터가 없을 때)
    }
}
package com.example.flowbit.ui.exchange

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flowbit.R
import com.example.flowbit.data.network.Exchange
import com.example.flowbit.data.network.ExchangeList
import com.example.flowbit.databinding.ItemExchangeRateBinding

class ExchangeAdapter(private var exchanges: List<Exchange>) :
    RecyclerView.Adapter<ExchangeAdapter.ExchangeViewHolder>() {

    inner class ExchangeViewHolder(private val binding: ItemExchangeRateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(exchange: Exchange) {
            // 암호화폐 이름 및 심볼 설정
            binding.currencyName.text = exchange.cryptoName
            binding.countryName.text = "${exchange.cryptoSymbol} / ${exchange.blockchainName}"

            // 가격(₩) 설정: 1보다 작으면 소수점 6자리, 1 이상이면 소수점 제거
            binding.exchangeRate.text = if (exchange.won < 1) {
                "₩${String.format("%,.6f", exchange.won)}"
            } else {
                "₩${String.format("%,d", exchange.won.toLong())}"
            }

            // 변동률 설정 (양수는 초록색, 음수는 빨간색)
            binding.changePercent.apply {
                text = "${String.format("%.2f", exchange.rate)}%"
                setTextColor(
                    if (exchange.rate > 0) binding.root.context.getColor(R.color.green)
                    else binding.root.context.getColor(R.color.red)
                )
                setCompoundDrawablesWithIntrinsicBounds(
                    if (exchange.rate > 0) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down,
                    0, 0, 0
                )
            }

            // 암호화폐 아이콘 로드
            Glide.with(binding.cryptoIcon.context)
                .load(exchange.cryptoIcon)
                .placeholder(R.drawable.placeholder_icon) // 로딩 중 표시할 기본 이미지
                .error(R.drawable.error_icon) // 로드 실패 시 표시할 이미지
                .into(binding.cryptoIcon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeViewHolder {
        val binding = ItemExchangeRateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExchangeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExchangeViewHolder, position: Int) {
        holder.bind(exchanges[position])
    }

    override fun getItemCount(): Int = exchanges.size

    fun updateData(newExchanges: List<Exchange>) {
        exchanges = newExchanges
        notifyDataSetChanged()
    }
}
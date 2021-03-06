package ru.redbyte.exchangerate.domain.exchange

import io.reactivex.Single
import ru.redbyte.exchangerate.data.exchange.Currency.*
import ru.redbyte.exchangerate.domain.ExchangeRate
import ru.redbyte.exchangerate.domain.SingleUseCase
import ru.redbyte.exchangerate.domain.UseCase.None
import javax.inject.Inject

class GetAllRates @Inject constructor(
    private val resultDataSource: ExchangeDataSource
) : SingleUseCase<List<ExchangeRate>, None>() {

    override fun execute(params: None): Single<List<ExchangeRate>> {
        val rub = resultDataSource.getRate(RUB, "$USD,$EUR,$GBP,$RUB")
        val usd = resultDataSource.getRate(USD, "$USD,$EUR,$GBP,$RUB")
        val eur = resultDataSource.getRate(EUR, "$USD,$GBP,$RUB") //SERVER BUG for base = EUR, if symbols =$USD,$EUR,$GBP then return  "error": "Symbols 'EUR,USD,GBP' are invalid for date 2019-11-05."
        val gbp = resultDataSource.getRate(GBP, "$USD,$EUR,$GBP,$RUB")
        return Single
            .merge(usd, eur, gbp, rub)
            .toList()
    }
}
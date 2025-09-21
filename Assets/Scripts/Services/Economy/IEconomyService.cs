using System.Threading.Tasks;

namespace PongRoyale.Services.Economy
{
    public interface IEconomyService
    {
        CurrencyBalance GetBalance();
        Task AddSoftCurrencyAsync(int amount);
        Task AddPremiumCurrencyAsync(int amount);
    }

    public readonly struct CurrencyBalance
    {
        public readonly int SoftCurrency;
        public readonly int PremiumCurrency;

        public CurrencyBalance(int softCurrency, int premiumCurrency)
        {
            SoftCurrency = softCurrency;
            PremiumCurrency = premiumCurrency;
        }
    }
}

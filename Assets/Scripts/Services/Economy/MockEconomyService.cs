using System.Threading.Tasks;
using UnityEngine;

namespace PongRoyale.Services.Economy
{
    [CreateAssetMenu(menuName = "Pong Royale/Services/Mock Economy")]
    public class MockEconomyService : ScriptableObject, IEconomyService
    {
        [SerializeField]
        private int softCurrency = 0;

        [SerializeField]
        private int premiumCurrency = 0;

        public CurrencyBalance GetBalance() => new(softCurrency, premiumCurrency);

        public Task AddSoftCurrencyAsync(int amount)
        {
            softCurrency += Mathf.Max(0, amount);
            return Task.CompletedTask;
        }

        public Task AddPremiumCurrencyAsync(int amount)
        {
            premiumCurrency += Mathf.Max(0, amount);
            return Task.CompletedTask;
        }
    }
}

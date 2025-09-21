using System.Threading.Tasks;
using UnityEngine;

namespace PongRoyale.Services.Monetization
{
    [CreateAssetMenu(menuName = "Pong Royale/Services/Mock IAP Service")]
    public class MockIapService : ScriptableObject, IIapService
    {
        public Task<bool> PurchaseProductAsync(string productId)
        {
            Debug.Log($"Mock purchase processed for product: {productId}");
            return Task.FromResult(true);
        }

        public Task RestorePurchasesAsync()
        {
            Debug.Log("Mock restore purchases executed");
            return Task.CompletedTask;
        }
    }
}

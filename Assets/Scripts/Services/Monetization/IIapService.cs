using System.Threading.Tasks;

namespace PongRoyale.Services.Monetization
{
    public interface IIapService
    {
        Task<bool> PurchaseProductAsync(string productId);
        Task RestorePurchasesAsync();
    }
}

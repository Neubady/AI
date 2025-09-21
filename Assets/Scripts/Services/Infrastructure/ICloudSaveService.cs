using System.Threading.Tasks;

namespace PongRoyale.Services.Infrastructure
{
    public interface ICloudSaveService
    {
        Task SaveAsync(string key, string data);
        Task<string?> LoadAsync(string key);
    }
}

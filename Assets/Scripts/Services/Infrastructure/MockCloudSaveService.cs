using System.Collections.Generic;
using System.Threading.Tasks;
using UnityEngine;

namespace PongRoyale.Services.Infrastructure
{
    [CreateAssetMenu(menuName = "Pong Royale/Services/Mock Cloud Save")]
    public class MockCloudSaveService : ScriptableObject, ICloudSaveService
    {
        private readonly Dictionary<string, string> _storage = new();

        public Task SaveAsync(string key, string data)
        {
            _storage[key] = data;
            return Task.CompletedTask;
        }

        public Task<string?> LoadAsync(string key)
        {
            _storage.TryGetValue(key, out var value);
            return Task.FromResult(value);
        }
    }
}

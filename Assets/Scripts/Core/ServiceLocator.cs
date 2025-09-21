using System;
using System.Collections.Generic;

namespace PongRoyale.Core
{
    /// <summary>
    /// Lightweight service locator that allows gameplay and UI systems to resolve
    /// interfaces for backend services such as networking or economy. This is not a
    /// full dependency injection framework but provides enough structure for the
    /// prototype and can be replaced by a DI solution later on.
    /// </summary>
    public static class ServiceLocator
    {
        private static readonly Dictionary<Type, object> _services = new();

        public static void Register<TService>(TService implementation)
        {
            var key = typeof(TService);
            _services[key] = implementation ?? throw new ArgumentNullException(nameof(implementation));
        }

        public static TService Resolve<TService>()
        {
            var key = typeof(TService);
            if (_services.TryGetValue(key, out var implementation))
            {
                return (TService)implementation;
            }

            throw new InvalidOperationException($"Service of type {key.Name} has not been registered");
        }

        public static bool TryResolve<TService>(out TService service)
        {
            var key = typeof(TService);
            if (_services.TryGetValue(key, out var implementation))
            {
                service = (TService)implementation;
                return true;
            }

            service = default!;
            return false;
        }

        public static void Clear()
        {
            _services.Clear();
        }
    }
}

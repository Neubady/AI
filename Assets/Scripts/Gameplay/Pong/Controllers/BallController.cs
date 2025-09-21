using UnityEngine;

namespace PongRoyale.Gameplay.Pong.Controllers
{
    [RequireComponent(typeof(Rigidbody2D))]
    public class BallController : MonoBehaviour
    {
        [SerializeField]
        private float initialSpeed = 10f;

        [SerializeField]
        private float speedIncreasePerHit = 0.5f;

        [SerializeField]
        private float maxSpeed = 20f;

        private Rigidbody2D _rigidbody = default!;
        private Vector2 _currentVelocity;
        private bool _autoLaunchOnStart = true;

        private void Awake()
        {
            _rigidbody = GetComponent<Rigidbody2D>();
        }

        private void Start()
        {
            if (_autoLaunchOnStart)
            {
                Launch(Vector2.right);
            }
        }

        public void Configure(float startSpeed, float hitSpeedIncrease, float maximumSpeed, bool autoLaunch = true)
        {
            initialSpeed = startSpeed;
            speedIncreasePerHit = hitSpeedIncrease;
            maxSpeed = maximumSpeed;
            _autoLaunchOnStart = autoLaunch;
        }

        public void Launch(Vector2 direction)
        {
            if (direction == Vector2.zero)
            {
                direction = Random.insideUnitCircle.normalized;
            }

            direction.Normalize();
            _currentVelocity = direction * initialSpeed;
            _rigidbody.velocity = _currentVelocity;
        }

        public void ResetBall(Vector2 direction)
        {
            transform.position = Vector3.zero;
            _rigidbody.velocity = Vector2.zero;
            Launch(direction);
        }

        private void OnCollisionEnter2D(Collision2D collision)
        {
            _currentVelocity = _rigidbody.velocity;
            _currentVelocity = Vector2.ClampMagnitude(_currentVelocity * (1f + speedIncreasePerHit), maxSpeed);
            _rigidbody.velocity = _currentVelocity;
        }
    }
}

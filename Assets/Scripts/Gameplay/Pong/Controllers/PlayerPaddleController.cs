using UnityEngine;

namespace PongRoyale.Gameplay.Pong.Controllers
{
    /// <summary>
    /// Handles input for the local player using touch controls. For desktop testing,
    /// mouse dragging and keyboard input are also supported.
    /// </summary>
    public class PlayerPaddleController : PaddleControllerBase
    {
        [SerializeField]
        private string verticalAxis = "Vertical";

        private float _targetY;

        protected override void Awake()
        {
            base.Awake();
            _targetY = Body.position.y;
        }

        private void Update()
        {
            UpdateFromTouch();
            UpdateFromMouse();
            UpdateFromKeyboard();
        }

        private void UpdateFromTouch()
        {
            if (Input.touchCount == 0)
            {
                return;
            }

            var touch = Input.GetTouch(0);
            var worldPoint = Camera.main!.ScreenToWorldPoint(touch.position);
            _targetY = worldPoint.y;
        }

        private void UpdateFromMouse()
        {
            if (!Input.GetMouseButton(0))
            {
                return;
            }

            var worldPoint = Camera.main!.ScreenToWorldPoint(Input.mousePosition);
            _targetY = worldPoint.y;
        }

        private void UpdateFromKeyboard()
        {
            var axis = Input.GetAxisRaw(verticalAxis);
            if (Mathf.Approximately(axis, 0f))
            {
                return;
            }

            _targetY += axis * Time.deltaTime * 10f;
        }

        protected override float GetTargetPosition()
        {
            return _targetY;
        }
    }
}

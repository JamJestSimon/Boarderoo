using Microsoft.AspNetCore.Mvc;
using BoarderooAPI.Model;
using BoarderooAPI.Service;
using BoarderooAPI.Requests;

namespace BoarderooAPI.Controllers;

[ApiController]
[Route("[controller]")]

public class LoginController : ControllerBase
{
    private readonly LoginService _loginService;
    private readonly AdminService _adminService;
    public LoginController(LoginService loginService, AdminService adminService)
    {
        this._loginService = loginService;
        this._adminService = adminService;
    }

    [HttpPost(Name = "Login")]
    public async Task<ActionResult> Login([FromBody] LoginRequest request)
    {
        var response = await _loginService.Login(request.Email, request.Password);
        return ConvertServiceResultToActionResult(response);
    }

    [HttpPost("OAuthLogin")]
    public async Task<ActionResult> OAuthLogin([FromBody] LoginOAuthRequest request)
    {
        var response = await _loginService.OAuthLogin(request.Email, request.Type);
        return ConvertServiceResultToActionResult(response);
    }

    [HttpPost("Admin")]
    public async Task<ActionResult> AdminLogin([FromBody] AdminLoginRequest request)
    {
        var response = await _adminService.Login(request.Login, request.Password);
        return ConvertServiceResultToActionResult(response);
    }

    private ActionResult ConvertServiceResultToActionResult<T>(ServiceResult<T> serviceResult)
{
    if (serviceResult.ResultCode==200)
    {
        return Ok(serviceResult); // Sukces
    }

    if (serviceResult.ResultCode is null)
    {
        return StatusCode(serviceResult.ResultCode ?? 500, new { Message = serviceResult.Message });
    }
    return BadRequest(serviceResult);
}
 }
using Microsoft.AspNetCore.Mvc;
using BoarderooAPI.Model;
using BoarderooAPI.Service;

namespace BoarderooAPI.Controllers;

 [ApiController]
 [Route("[controller]")]

 public class LoginController:ControllerBase
 {
    private readonly LoginService _loginService;
    public LoginController(LoginService loginService)
    {
        this._loginService=loginService;
    }

    [HttpPost(Name = "Login")]
    public async Task<ActionResult> Login([FromBody] LoginRequest request)
    {
        var response=await _loginService.Login(request.Email,request.Password); 
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
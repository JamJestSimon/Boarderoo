using Microsoft.AspNetCore.Mvc;
using BoarderooAPI.Model;
using BoarderooAPI.Service;

namespace BoarderooAPI.Controllers;

 [ApiController]
 [Route("[controller]")]

 public class OrderController:ControllerBase
 {
    private readonly OrderService _orderService;
    public OrderController(OrderService orderService)
    {
        this._orderService=orderService;
    }

    [HttpPost(Name = "PostOrder")]
    
    public async Task<ActionResult> AddOrder(OrderDocument order)
    {

        var response=await _orderService.AddOrder(order); 
        return ConvertServiceResultToActionResult(response);
    }

    [HttpGet]
    [Route("{id}")]
    public async Task<ActionResult> GetOrder(string id)
    {
        var response=await _orderService.GetOrder(id);
        return ConvertServiceResultToActionResult(response);
    }
    [HttpDelete]
    [Route("{id}")]
    public async Task<ActionResult> DeleteOrder(string id)
    {

            var response=await _orderService.DeleteOrder(id);
            return ConvertServiceResultToActionResult(response);

    }
    [HttpGet(Name = "GetOrders")]
    public async Task<ActionResult> GetAllOrders()
    {
        var response=await _orderService.GetAllOrders(); 
        return ConvertServiceResultToActionResult(response);
    }


    [HttpGet]
    [Route("/Order/user/{email}")]
    public async Task<ActionResult> GetOrdersByUser(string email)
    {
      
        var response=await _orderService.GetOrderByUser(email); 
        return ConvertServiceResultToActionResult(response);
    }

    [HttpPut]
    public async Task<ActionResult> UpdateStatus([FromQuery] string id, [FromQuery] string status)
    {
        var response = await _orderService.UpdateStatus(id, status);
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
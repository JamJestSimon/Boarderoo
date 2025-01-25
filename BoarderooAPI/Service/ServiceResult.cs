public class ServiceResult<T>
{
    public string Message {get; set;}
    public int? ResultCode {get; set;}
    public T Data {get; set;}
}
using System;
using System.Security.Cryptography;
using System.Text;
namespace BoarderooAPI.Service;

public static class HashService
{

    public static string hashfunction(string text)
    {
       using (SHA256 sha256Hash = SHA256.Create()) {
                byte[] bytes = sha256Hash.ComputeHash(Encoding.UTF8.GetBytes(text));
                StringBuilder builder = new StringBuilder();
                foreach (byte b in bytes) {
                    builder.Append(b.ToString("x2")); // Convert to hexadecimal string
                }
                return builder.ToString();
    }}
    public static bool compare(string hash1,string hash2)
    {
    if (hash1.Length == hash2.Length)
    {
    int i=0;
    while ((i < hash1.Length) && (hash1[i] == hash2[i]))
    {
        i += 1;
    }
    if (i == hash1.Length)
    {
        return true;
    }
    else return false;
    }
    else return false;


    }
}
using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Text.Json.Serialization;

namespace c_sharp_interop
{
    public class RemoteMethodCaller
    {
        private static HttpClient client = new HttpClient();

        public TOutput Call<TInput, TOutput>(TInput @object, string methodName, IEnumerable arguments, string endpoint, string className = "")
        {
            if (string.IsNullOrWhiteSpace(className)) {
                className = typeof (TInput).Name;
            }
            var args = arguments.Cast<object>().ToArray();

            var data = new Dictionary<String, object>();
            data["object"] = @object;
            data["arguments"] = args;

            var json = JsonSerializer.Serialize(data);
            var input = new StringContent(json, Encoding.UTF8, "application/json");
            var task = client.PostAsync(new Uri(new Uri(endpoint),new Uri($"/{className}/{methodName}")), input);
            task.Wait();
            var message = task.Result;
            var resultTask = message.Content.ReadAsStringAsync();
            resultTask.Wait();

            var response = (TOutput) JsonSerializer.Deserialize(resultTask.Result, typeof (TOutput));
            return response;
        }
        
        public TOutput New<TOutput>(string className, IEnumerable arguments, string endpoint)
        {
            var args = arguments.Cast<object>().ToArray();

            var data = new Dictionary<String, object>();
            data["arguments"] = args;

            var input = new StringContent(JsonSerializer.Serialize(data), Encoding.UTF8, "application/json");
            var task = client.PostAsync(new Uri(new Uri(endpoint),new Uri($"/{className}")), input);
            task.Wait();
            var message = task.Result;
            var result = message.Content.ReadAsStringAsync();
            result.Wait();

            var response = (TOutput) JsonSerializer.Deserialize(result.Result, typeof (TOutput));
            return response;
        }
    }
}

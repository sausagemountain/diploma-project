using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Text.Json.Serialization;

namespace c_sharp_interop_client
{
    public class RemoteMethodCaller
    {
        private static HttpClient client = new HttpClient();

        public TOutput Call<TInput, TOutput>(TInput @object, string methodName, IEnumerable arguments, string endpoint)
        {
            string className = typeof (TInput).Name;
            var args = arguments.Cast<object>().ToArray();

            var data = new Dictionary<String, object>();
            data["class"] = className;
            data["object"] = @object;
            data["method"] = methodName;
            data["arguments"] = args;

            var json = JsonSerializer.Serialize(data);
            var input = new StringContent(json, Encoding.UTF8, "application/json");
            var task = client.PostAsync(endpoint, input);
            task.Wait();
            var message = task.Result;
            var result = message.Content.ReadAsStringAsync();
            result.Wait();

            var response = (TOutput) JsonSerializer.Deserialize(result.Result, typeof (TOutput));
            return response;
        }
    }
}

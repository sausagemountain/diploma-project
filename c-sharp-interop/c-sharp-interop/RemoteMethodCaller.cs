using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;

namespace c_sharp_interop
{
    public class RemoteMethodCaller
    {
        private static readonly HttpClient client = new HttpClient();

        public TOutput Call<TInput, TOutput>(TInput @object, string methodName, 
                                             IEnumerable arguments, string endpoint, string className = "")
        {
            if (string.IsNullOrWhiteSpace(className))
                className = typeof (TInput).Name;
            object[] args = arguments.Cast<object>().ToArray();

            var data = new Dictionary<string, object>();
            data["object"] = @object;
            data["arguments"] = args;

            string json = JsonSerializer.Serialize(data);
            var input = new StringContent(json, Encoding.UTF8, "application/json");
            Task<HttpResponseMessage> task = client.PostAsync(new Uri(new Uri(endpoint), new Uri($"/{className}/{methodName}")), input);
            task.Wait();
            HttpResponseMessage message = task.Result;
            Task<string> resultTask = message.Content.ReadAsStringAsync();
            resultTask.Wait();

            var response = (TOutput) JsonSerializer.Deserialize(resultTask.Result, typeof (TOutput));
            return response;
        }

        public TOutput New<TOutput>(string className, IEnumerable arguments, string endpoint)
        {
            object[] args = arguments.Cast<object>().ToArray();

            var data = new Dictionary<string, object>();
            data["arguments"] = args;

            var input = new StringContent(JsonSerializer.Serialize(data), Encoding.UTF8, "application/json");
            Task<HttpResponseMessage> task = client.PostAsync(new Uri(new Uri(endpoint), new Uri($"/{className}")), input);
            task.Wait();
            HttpResponseMessage message = task.Result;
            Task<string> result = message.Content.ReadAsStringAsync();
            result.Wait();

            var response = (TOutput) JsonSerializer.Deserialize(result.Result, typeof (TOutput));
            return response;
        }
    }
}
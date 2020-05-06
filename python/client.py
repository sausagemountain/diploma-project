import json
import http.client as http_client


def send(object, class_name: str, arguments: list, method_name: str, endpoint: str):
    conn = http_client.HTTPConnection(endpoint)
    obj = json.dumps({
        'class': class_name,
        'object': object,
        'method': str,
        'arguments': arguments
    })
    conn.connect()
    conn.request('POST', '/', obj)
    response = json.loads(conn.getresponse().msg.get_payload())
    return response

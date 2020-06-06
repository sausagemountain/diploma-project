def call_method(object, id: str, class_name: str, method_name: str, arguments: list, endpoint: str):
    import http.client as http_client
    import json
    conn = http_client.HTTPConnection(endpoint)
    obj = json.dumps({
        'id': id,
        'object': object,
        'arguments': arguments
    })
    conn.connect()
    conn.request('POST', f'/api/{class_name}/{method_name}', obj)
    response = json.loads(conn.getresponse().msg.get_payload())
    conn.close()
    return response


def new_object(class_name: str, arguments: list, endpoint: str):
    import http.client as http_client
    import json
    conn = http_client.HTTPConnection(endpoint)
    obj = json.dumps({
        'arguments': arguments
    })
    conn.connect()
    conn.request('POST', f'/api/{class_name}', obj)
    response = json.loads(conn.getresponse().msg.get_payload())
    conn.close()
    return response

from flask import Flask, request, Response, jsonify

app = Flask(__name__)


@app.route('/', methods=['GET', 'POST'])
def hello_world():
    if request.method == 'GET':
        return jsonify(['hello', 'world'])
    else:
        try:
            clazzName = request.json['class']
            object = request.json['object']
            methodName = request.json['method']
            arguments = request.json['arguments']

            clazz = type(clazzName)
            method = getattr(clazz, methodName)
            result = method(object, arguments)

            return {
                'result': result
            }
        except Exception:
            return {
                'error': 'cannot complete operation'
            }


if __name__ == '__main__':
    app.run()

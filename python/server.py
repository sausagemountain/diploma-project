from flask import Flask, request, Response, jsonify

app = Flask(__name__)

all_objects = dict()


def generate_id(length: int):
    return ""


@app.route('/<class_name>', methods=['POST'])
def hello_world(class_name: str):
    try:
        arguments = request.json['arguments']

        clazz = type(class_name)
        if arguments:
            result = clazz(*arguments)
        else:
            result = clazz()

        id = None
        while id is not None and ' '.join([class_name, id]) not in all_objects.keys():
            id = generate_id(10)
        internal_id = ' '.join([class_name, id])

        all_objects[internal_id] = result

        return {
            'id': id,
            'result': result
        }
    except Exception:
        return {
            'error': 'cannot complete operation'
        }


@app.route('/<class_name>/<method_name>', methods=['POST'])
def hello_world(class_name: str, method_name: str):
    try:
        id = request.json['id']
        internal_id = ' '.join([class_name, id])
        object = request.json['object']
        if object is not None:
            all_objects[internal_id] = object
        else:
            object = all_objects[internal_id]

        arguments = request.json['arguments']

        clazz = type(class_name)
        method = getattr(clazz, method_name)
        if object is not None:
            result = method(object, *arguments)
        else:
            return None

        return {
            'id': id,
            'result': result
        }
    except Exception:
        return {
            'error': 'cannot complete operation'
        }


if __name__ == '__main__':
    app.run()

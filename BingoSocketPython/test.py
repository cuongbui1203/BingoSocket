import asyncio
import json
import time
from io import StringIO
import socket
import websockets
from websockets import connect
from threading import Lock, Thread

lock = Lock()

message_list = []  # global


# nhận thông tin về game
async def ws_game():
    global message_list
    bln_running = True
    # ws_a = websockets.connect("ws://127.0.0.1:10637/game/bingo")
    async with connect("ws://127.0.0.1:10637/game/bingo") as ws_a:
        print(ws_a)
        io = StringIO()
        data = {
            'type': 14,
            'select': 0,
            'sender': "python",
            'table': [-1]
        }
        json.dump(data, io)
        print(io.getvalue())

        await ws_a.send(io.getvalue())

        while bln_running:
            response_a = await ws_a.recv()
            # dataRecv = json.loads(msg)
            print(response_a)
            lock.acquire()
            message_list.append(response_a)
            lock.release()


# Gửi thông tin game cho web
async def ws_web():
    global message_list
    bln_running = True
    # ws_b = websockets.connect("ws://127.0.0.1:12345/")
    print("start")
    async with websockets.connect("ws://104.194.240.16/ws/channels/") as ws_b:
        # async with websockets.connect("ws://127.0.0.1:12345/") as ws_b:
        print(ws_b)
        objSendEnd = {
            'result': 3,
            "id1": 1,
            "id2": 99999,
            'match': 292
        }

        objSendUpdate = {
            "result": 2,
            "id1": 1,
            "id2": 99999,
            "match": 292,
            "status": 1
        }
        objSendStart = {
            'result': 1,
            'id1': 1,
            'id2': 23,
            'match': 292,
            'status': 1
        }
        # time.sleep(2)

        await ws_b.send(json.dumps(objSendStart))
        print(json.dumps(objSendStart))
        print('Send Start packet')
        print(await ws_b.recv())
        time.sleep(2)

        await ws_b.send(json.dumps(objSendUpdate))
        print(json.dumps(objSendUpdate))
        print('Send Update packet')
        print(await ws_b.recv())
        time.sleep(2)

        await ws_b.send(json.dumps(objSendEnd))
        print(json.dumps(objSendEnd))
        print('Send End packet')
        print(await ws_b.recv())


asyncio.run(ws_web())

#
# def between_callback_web():
#     loop = asyncio.new_event_loop()
#     asyncio.set_event_loop(loop)
#
#     loop.run_until_complete(ws_web())
#     loop.close()
#
#
# # ------------main-------------
# threads = []
#
# for func in [between_callback_web()]:
#     threads.append(Thread(target=func))
#     # threads[-1].start()
#
# for i in threads:
#     i.start()
#     i.join()

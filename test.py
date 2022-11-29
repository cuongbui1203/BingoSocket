import socket
import struct


sever = socket.socket()
try:
    sever.bind(('127.0.0.1', 8881))
except:
    sever.shutdown(1)
    # pass
sever.listen(10)
con, add = sever.accept()
print(add)
pack = con.recv(1024)
print(pack)

if pack is not None:
    index = 0
    action = struct.unpack('i', pack[index:index+4])[0]
    print(action)
    try:
        index = 0
        if action == 1:
            index += 4
            lenIP = struct.unpack('i', pack[index:index + 4])[0]
            print(lenIP)
            index += 4
            ip = struct.unpack(
                f"{lenIP}s", pack[index:index + lenIP])[0].decode()
            print(ip)
            index += lenIP
            port = struct.unpack('i', pack[index:index + 4])[0]
            index += 4
            print(port)
            lenGame = struct.unpack('i', pack[index:index + 4])[0]
            index += 4
            print(lenGame)

            game = struct.unpack(
                f'{lenGame}s', pack[index:index + lenGame])[0].decode()
            print(game)
            index += lenGame
            lenRule = struct.unpack('i', pack[index:index + 4])[0]
            print(lenRule)
            index += 4
            rule = struct.unpack(
                f'{lenRule}s', pack[index:index + lenRule])[0].decode()
            print(rule)
            index += lenRule
            lenAuthor = struct.unpack('i', pack[index:index + 4])[0]
            print(lenAuthor)
            index += lenAuthor
            author = struct.unpack(
                f'{lenAuthor}s', pack[index:index + lenAuthor])[0].decode()
            print(author)

    except:
        pass

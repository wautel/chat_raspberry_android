import sys, socket, select
import fcntl, os
import errno
import time
from tkinter import *

host = "localhost"
port = 9009

fenetre = Tk()
label = Label(fenetre, text="Pas de message")
label.pack()

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
try :
	s.connect((host, port))
except	:
	print ("Impossible de se connecter")
	sys.exit()

fcntl.fcntl(s, fcntl.F_SETFL, os.O_NONBLOCK)

def 	connect():
	while 1:
		fenetre.update_idletasks()
		fenetre.update()
		try:
			msg = s.recv(4096)
		except socket.error as e:
			err = e.args[0]
			if err == errno.EAGAIN or err == errno.EWOULDBLOCK:
				time.sleep(1)
				continue
			else:
				# Erreur
				print ("Erreur Socket")
				sys.exit(1)
		else:
			# nouveau message
			label["text"] = msg

def 	sendmessage():
		msg = "Tout va bien\n"
		msg_byte = str.encode(msg)
		s.send(msg_byte)

def 	sendmessage2():
		msg = "J'ai un probleme\n"
		msg_byte = str.encode(msg)
		s.send(msg_byte)
		
bouton=Button(fenetre, text="Tout va bien", command=sendmessage, height=10, width=17, background="blue")
bouton.pack();
bouton2=Button(fenetre, text="J'ai un probleme", command=sendmessage2, height=10, width=17, background="red")
bouton2.pack();
# fenetre.mainloop()
connect()
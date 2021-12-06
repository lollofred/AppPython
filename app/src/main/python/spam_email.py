import smtplib
def main(email,passw,email2,oggetto,contenuto,NUMBER_OF_EMAILS):
	server = smtplib.SMTP('smtp.gmail.com:587')
	server.starttls()

	server.login(email, passw)
	
	sent = "All sent correctly"

	msg = "Subject:"+oggetto+"\n\n"+contenuto

	for i in range(NUMBER_OF_EMAILS):
		server.sendmail(email, email2, msg)
		print(i+1, "sent!")
	server.quit()
	
	return sent

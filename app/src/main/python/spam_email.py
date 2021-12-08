import smtplib
def main(email,passw,email2,oggetto,contenuto,NUMBER_OF_EMAILS):
	try:
		server = smtplib.SMTP('smtp.gmail.com:587')
		server.starttls()

		server.login(email, passw)
		
		output = ""
		arremail2 = email2.split(',')
		msg = "Subject:"+oggetto+"\n\n"+contenuto

		for i in range(NUMBER_OF_EMAILS):
			for email2 in arremail2:
				server.sendmail(email, email2, msg)
				output +=(f'{i+1} email sent to {email2} \n')
				# print(f'{i+1} email sent to {email2}')
		server.quit()

	except Exception as e:
		if(e.args[0] == 535):
			output = "Error - Invalid Username or Password"
		else:
			output = "Error - Invalid victim email"
		
	return output


const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

const db = admin.firestore();

exports.modifyUser = functions.database
    .ref("status/{userID}")
    .onWrite(async (change, context) => {
        try {

            const document = change.after.exists ? change.after.val() : null;

            if (document !== null) {
                await db.collection('users').doc(document.email).set({
                    online: document.online
                }, {merge: true});
                const contactsRef = await db.collection(`users/${document.email}/contacts`).get();
                contactsRef.forEach(async doc => {
                    const userOnlineRef = await db.doc(`users/${doc.id}/contacts/${document.email}`);

                    await userOnlineRef.set({
                        online: document.online
                    }, {merge: true});

                });

                return contactsRef;
            }

        } catch (err) {
            console.log('Error getting documents', err);
        }

    });

exports.updateProfile = functions.firestore
    .document('users/{userID}')
    .onWrite(async (snap, context) => {
        try {
            const profile = snap.after.exists ? snap.after.data() : null;
            if (profile !== null) {
                const contactsRef = await db.collection(`users/${context.params.userID}/contacts`).get();
                contactsRef.forEach(async doc => {
                    const userOnlineRef = await db.doc(`users/${doc.id}/contacts/${context.params.userID}`);

                    await userOnlineRef.set({
                        nickname: profile.nickname,
                        photoUrl: profile.photoUrl
                    }, {merge: true});

                });
            }
        } catch (err) {
            console.log('Error getting documents', err);
        }
    });

exports.addNewContact = functions.firestore
    .document('users/{userID}/contacts/{contactID}')
    .onCreate(async (snap, context) => {
        try {
            const user = await db.doc(`users/${context.params.userID}`).get();
            if (user.exists) {
                var lastMessage = "";
                var lastTimeMessage = 0;

                var chatKey = context.params.userID + "___" + context.params.contactID;
                if (context.params.userID.localeCompare(context.params.contactID) > 0) {
                    chatKey = context.params.contactID + "___" + context.params.userID;
                }

                const lMessage = await db.collection(`chats/${chatKey}/messages`).orderBy("timestamp", "desc").limit(1).get();
                lMessage.forEach(async doc => {
                    if (doc.exists) {
                        lastMessage = doc.data().message;
                        lastTimeMessage = doc.data().timestamp;
                    }
                });

                await db.doc(`users/${context.params.contactID}/contacts/${context.params.userID}`).set({
                    lastMessage: lastMessage,
                    lastTimeMessage: lastTimeMessage,
                    online: user.data().online
                }, {merge: true});

                const contact = await db.doc(`users/${context.params.contactID}`).get();

                await snap.ref.set(
                    {
                        online: contact.data().online
                    }, {merge: true}
                );
            }

        } catch (err) {
            console.log('Error getting documents', err);
        }
    });

exports.removeContact = functions.firestore
    .document('users/{userID}/contacts/{contactID}')
    .onDelete(async (snap, context) => {
        try {

            await db.doc(`users/${context.params.contactID}/contacts/${context.params.userID}`).delete();

        } catch (err) {
            console.log('Error getting documents', err);
        }
    });

exports.newMessage = functions.firestore
    .document('chats/{chatID}/messages/{messageID}')
    .onCreate(async (snap, context) => {
        try {

            const message = snap.data();
            const contactReceiver = db.doc(`users/${message.receiver}/contacts/${message.sender}`);
            const contactSender = db.doc(`users/${message.sender}/contacts/${message.receiver}`);
            if (!message.read) {
                var lastMsg = message.message;
                if (message.image) {
                    lastMsg = "(Image)"
                }
                await contactReceiver.set({
                    lastMessage: lastMsg,
                    lastTimeMessage: message.timestamp
                }, {merge: true});

                await contactSender.set({
                    lastMessage: lastMsg,
                    lastTimeMessage: message.timestamp
                }, {merge: true});
            }

            const receiverToken = await db.doc(`users/${message.receiver}`).get();

            if (receiverToken.exists) {
                if (receiverToken.data().nToken !== "") {
                    const payload = {
                        data: {
                            sender: message.sender,
                            message: message.message
                        }
                    };

                    console.log(receiverToken.data().nToken);

                    const respons = await admin.messaging().sendToDevice(receiverToken.data().nToken, payload);
                }
            }

        } catch (err) {
            console.log('Error getting documents', err);
        }
    });

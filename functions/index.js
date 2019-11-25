const functions = require('firebase-functions');
const admin = require('firebase-admin');

//admin.initializeApp(functions.config().firebase);
admin.initializeApp();

let db = admin.firestore();

exports.getMostRecentMoodEvent = functions.https.onCall((data, context) =>
    {
        const uid = data.uid;
        const filters = data.filters;

        if (uid == null)
        {
            console.log('tried to access most recent mood event of null user');
        }

        let user = db.collection('users').doc(uid);

        return user.get().then((snapshot) =>
            {
                if (!snapshot.exists)
                {
                    console.log('this user doesn\'t exist');
                    return null;
                }
                else
                {
                    let result = null;
                    if (filters == null || filters[0] == '')
                    {
                        return snapshot.data().moods[0];
                    }
                    for (const mood of snapshot.data().moods)
                    {
                        if (filters.includes(mood.emotion))
                        {
                            result = mood;
                            break;
                        }
                    }
                    return result;
                }
            })
            .catch((error) =>
                {
                    console.log('error occurred: ' + error);
                    return null;
                });
    });

exports.searchUsers = functions.https.onCall((data, context) =>
{
    let search = data.search.toLowerCase();
    let uids = [];
    return db.collection('users').orderBy('username')
        .startAt(search)
        .endAt(search + '\uf8ff').get().then(snapshot =>
        {
            snapshot.forEach(doc =>
                {
                    console.log('returning ' + doc.id);
                    uids.push(doc.id);
                });

            return uids;
        });
});

exports.deleteUser = functions.https.onCall((data, context) =>
{
    let uid = data.uid;
    let username = data.username;

    console.log('searching for references to ' + uid);
    db.collection('users').get().then(snapshot => {
        snapshot.forEach(doc => {
            let followed = doc.data().following_list;
            let requests = doc.data().requested_list;

            let changed_followed = false;
            let changed_requests = false;

            for (let i = 0; i < followed.length; i++)
            {
                if (followed[i] == uid)
                {
                    console.log(doc.data().username + ' follows ' + uid);
                    followed.splice(i, 1);
                    changed_followed = true;
                    break;
                }
            }

            for (let i = 0; i < requests.length; i++)
            {
                if (requests[i] == uid)
                {
                    //console.log('element ' + i + ' is ' + uid);
                    console.log(uid + ' has requested to follow ' + doc.data().username);
                    requests.splice(i, 1);
                    changed_requests = true;
                    break;
                }
            }

            if (changed_followed)
            {
                console.log('removing following from ' + doc.data().username);
                db.collection('users').doc(doc.id).update({following_list: followed});
            }

            if (changed_requests)
            {
                console.log('removing requested from ' + doc.data().username);
                db.collection('users').doc(doc.id).update({requested_list: requests});
            }
        });
    }).catch(err => {
        console.log('Error getting documents', err);
    });

    // Remove the Document from Firestore
    db.collection('users').doc(uid).delete().then(() => {}).catch(error =>
    {
        console.log('error deleting doc, ', error);
    });

    // Remove the user from Auth
    admin.auth().deleteUser(uid).then(() => {
        console.log('successfully deleted user');
    }).catch(err => {
        console.log('could not delete user');
    });

    admin.storage().bucket().file('image/' + username + '.png').delete().then(() =>
    {
        console.log('deleted image');
    }).catch(error =>
    {
        console.log('error deleting image', error);
    });
});
